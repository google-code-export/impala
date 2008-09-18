package org.impalaframework.web.servlet.wrapper;

import java.net.URL;

import javax.servlet.ServletContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.util.ObjectUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.Assert;

public class ModuleAwareServletContextWrapper implements ServletContextWrapper {

	public ServletContext wrapServletContext(ServletContext servletContext,
			ModuleDefinition moduleDefinition, 
			ClassLoader classLoader) {
		
		//FIXME implement and test, with integration test. Use in example
		
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setInterfaces(new Class[]{ServletContext.class});
		proxyFactory.setTarget(servletContext);
		proxyFactory.addAdvice(new ServletContextMethodInterceptor(moduleDefinition.getName(), servletContext, classLoader));
		return ObjectUtils.cast(proxyFactory.getProxy(), ServletContext.class);
	}
	
	private static class ServletContextMethodInterceptor implements MethodInterceptor {
		
		//FIXME test
		private final String moduleName;
		private final ServletContext rawservletContext;
		private final ClassLoader moduleClassLoader;
		
		public ServletContextMethodInterceptor(String moduleName, 
				ServletContext rawServletContext,
				ClassLoader moduleClassLoader) {
			super();
			Assert.notNull(moduleName);
			Assert.notNull(rawServletContext);
			this.moduleName = moduleName;
			this.rawservletContext = rawServletContext;
			this.moduleClassLoader = moduleClassLoader;
		}

		public Object invoke(MethodInvocation invocation) throws Throwable {
			System.out.println("Invoking " + invocation + " with " + moduleName + " servletContext " + rawservletContext + " classLoader " + moduleClassLoader);
			
			String methodName = invocation.getMethod().getName();
			if ("getResource".equals(methodName)) {
				String resourceName = (String) invocation.getArguments()[0];
				
				//remove the leading slash
				if (resourceName.startsWith("/")) {
					resourceName = resourceName.substring(1);
				}
				
				URL resource = moduleClassLoader.getResource(resourceName);
				if (resource != null) return resource;
			}
			//if setAttribute, then wrap item (if necessary), including the date of the session
			
			//if getAttribute, then unwrap item. Check that class loader is visible to module class loader
			
			return invocation.proceed();
		}
	}

}
