package net.java.impala.spring.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import net.java.impala.classloader.ContextResourceHelper;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.resolver.WebContextResourceHelper;
import net.java.impala.spring.util.DefaultApplicationContextLoader;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;

public class DefaultWebApplicationContextLoader extends DefaultApplicationContextLoader implements
		WebApplicationContextLoader {

	public DefaultWebApplicationContextLoader(WebContextResourceHelper resourceHelper) {
		super(resourceHelper);
	}

	@Override
	protected GenericApplicationContext newApplicationContext(ApplicationContext parent,
			DefaultListableBeanFactory beanFactory) {
		// relies on the fact that parent context are loaded as web contexts
		// while child contexts are not
		GenericApplicationContext context = parent != null ? new GenericApplicationContext(beanFactory, parent)
				: new GenericWebApplicationContext(beanFactory);
		return context;
	}

	public WebApplicationContext loadParentWebContext(WebApplicationContext parent, PluginSpec pluginSpec,
			ServletContext servletContext) {
		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();
		WebContextResourceHelper webContextResourceHelper = getWebContextResourceHelper();
		ClassLoader webClassLoader = webContextResourceHelper.getWebClassLoader(pluginSpec.getName());

		try {
			Thread.currentThread().setContextClassLoader(webClassLoader);
			return this.loadWebApplicationContext(parent, servletContext, getResourceLocations(servletContext, pluginSpec.getContextLocations()), webClassLoader);
		}
		finally {
			Thread.currentThread().setContextClassLoader(existingClassLoader);
		}
	}

	WebApplicationContext loadWebApplicationContext(WebApplicationContext parent, ServletContext servletContext,
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

	protected WebContextResourceHelper getWebContextResourceHelper() {
		ContextResourceHelper contextResourceHelper = super.getContextResourceHelper();

		Assert.isTrue(contextResourceHelper instanceof WebContextResourceHelper, ContextResourceHelper.class
				.getSimpleName()
				+ " " + contextResourceHelper + " must be instanceof " + WebContextResourceHelper.class);
		
		WebContextResourceHelper webContextResourceHelper = ((WebContextResourceHelper) getContextResourceHelper());
		return webContextResourceHelper;
	}
	
	//FIXME add test
	private static List<Resource> getResourceLocations(ServletContext servletContext, String[] locations) {
		List<Resource> resources = new ArrayList<Resource>();
		for (String location : locations) {
			ServletContextResource resource = new ServletContextResource(servletContext, location);
			resources.add(resource);
		}
		return resources;
	}


}
