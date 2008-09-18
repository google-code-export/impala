/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.web.helper.ImpalaServletUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

/**
 * Class with responsibility for wrapping HttpServletRequest and other 
 * Servlet API objects to provide module isolation
 * @author Phil Zoio
 */
public class ModuleServletWrapperFactory implements ServletContextAware {
	
	//FIXME add tests for this entire class
	
	private ServletContext servletContext;
	
	public HttpServletRequest wrapRequest(HttpServletRequest request) {
		
		//figure out which module we are dealing with
		String moduleName = getModuleName(request, servletContext);
		ModuleManagementFactory factory = ImpalaServletUtils.getModuleManagementFactory(servletContext);
		ConfigurableApplicationContext moduleContext = factory.getModuleStateHolder().getModule(moduleName);
		
		//FIXME add null checks
		long startupDate = moduleContext.getStartupDate();

		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setInterfaces(new Class[]{HttpServletRequest.class});
		proxyFactory.setTarget(request);
		proxyFactory.addAdvice(new RequestMethodInterceptor(moduleName, servletContext));
		
	
		
		return (HttpServletRequest) proxyFactory.getProxy();
	}

	private String getModuleName(HttpServletRequest request,
			ServletContext servletContext) {
		//TODO probably need to figure out the module name in a couple of ways, using subclasses
		return null;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		
		//wrap the servlet context with the 
	}
	
	private static class RequestMethodInterceptor implements MethodInterceptor {
		
		private final String moduleName;
		private final ServletContext servletContext;
		
		public RequestMethodInterceptor(String moduleName, ServletContext servletContext) {
			super();
			Assert.notNull(moduleName);
			Assert.notNull(servletContext);
			this.moduleName = moduleName;
			this.servletContext = servletContext;
		}

		public Object invoke(MethodInvocation invocation) throws Throwable {
			
			Object this1 = invocation.getThis();
			
			System.out.println("Invoking " + invocation + " on this " + this1);
			
			//if getSession return wrapped session
			
			return invocation.proceed();
		}
	}
	
	private static class SessionMethodInterceptor implements MethodInterceptor {
		
		private final String moduleName;
		private final ServletContext servletContext;
		
		public SessionMethodInterceptor(String moduleName, ServletContext servletContext) {
			super();
			Assert.notNull(moduleName);
			Assert.notNull(servletContext);
			this.moduleName = moduleName;
			this.servletContext = servletContext;
		}

		public Object invoke(MethodInvocation invocation) throws Throwable {
			System.out.println("Invoking " + invocation);
			
			//if setAttribute, then wrap item (if necessary), including the date of the session
			
			//if getAttribute, then unwrap item. Check that class loader is visible to module class loader
			
			return invocation.proceed();
		}
	}

}

