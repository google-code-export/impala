package net.java.impala.spring.web;

import java.util.List;

import javax.servlet.ServletContext;

import net.java.impala.classloader.ContextResourceHelper;
import net.java.impala.spring.resolver.WebContextResourceHelper;
import net.java.impala.spring.util.DefaultApplicationContextLoader;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class DefaultWebApplicationContextLoader extends DefaultApplicationContextLoader implements WebApplicationContextLoader {

	public DefaultWebApplicationContextLoader(ContextResourceHelper resourceHelper) {
		super(resourceHelper);
	}

	@Override
	protected GenericApplicationContext newApplicationContext(ApplicationContext parent, DefaultListableBeanFactory beanFactory) {
		//relies on the fact that parent context are loaded as web contexts while child contexts are not
		GenericApplicationContext context = parent != null 
		? new GenericApplicationContext(beanFactory, parent)
		: new GenericWebApplicationContext(beanFactory);
		return context;
	}

	public WebApplicationContext loadWebContext(WebApplicationContext parent, String parentName, String servletName,
			ServletContext context, List<Resource> resourceLocations) {
		
		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();
		WebContextResourceHelper webContextResourceHelper = getWebContextResourceHelper();
		ClassLoader webClassLoader = webContextResourceHelper.getWebClassLoader(parentName + "-" + servletName);

		try {
			Thread.currentThread().setContextClassLoader(webClassLoader);
			return this.loadWebApplicationContext(parent, context, resourceLocations, webClassLoader);
		}
		finally {
			Thread.currentThread().setContextClassLoader(existingClassLoader);
		}
	}

	public WebApplicationContext loadWebApplicationContext(WebApplicationContext parent, ServletContext servletContext,
			List<Resource> resourceLocations, ClassLoader classLoader) {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		if (parent != null)
			beanFactory.setBeanClassLoader(classLoader);

		// create the application context, and set the class loader
		GenericWebApplicationContext context = new GenericWebApplicationContext(beanFactory);

		context.setParent(parent);

		if (classLoader != null) {
			context.setClassLoader(classLoader);
		}

		context.setServletContext(servletContext);

		// create the bean definition reader
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);

		for (Resource resource : resourceLocations) {
			xmlReader.loadBeanDefinitions(resource);
		}

		context.refresh();
		return context;
	}

	//FIXME add test
	protected WebContextResourceHelper getWebContextResourceHelper() {
		ContextResourceHelper contextResourceHelper = super.getContextResourceHelper();

		if (!(contextResourceHelper instanceof WebContextResourceHelper)) {
			throw new IllegalStateException(ContextResourceHelper.class.getSimpleName() + " "
					+ contextResourceHelper.getClass().getName() + " not instance of "
					+ WebContextResourceHelper.class.getSimpleName());
		}
		WebContextResourceHelper webContextResourceHelper = ((WebContextResourceHelper) getContextResourceHelper());
		return webContextResourceHelper;
	}

}
