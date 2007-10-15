package net.java.impala.spring.web;

import javax.servlet.ServletContext;

import net.java.impala.location.ClassLocationResolver;
import net.java.impala.spring.plugin.ParentPluginLoader;
import net.java.impala.spring.plugin.PluginLoader;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class ParentWebPluginLoader extends ParentPluginLoader implements PluginLoader {

	private ServletContext servletContext;
	
	public ParentWebPluginLoader(ClassLocationResolver classLocationResolver, ServletContext servletContext) {
		super(classLocationResolver);
		Assert.notNull(servletContext, "ServletContext cannot be null");
		this.servletContext = servletContext;
	}

	@Override
	public GenericWebApplicationContext newApplicationContext(ApplicationContext parent, ClassLoader classLoader) {
		final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);
		
		final GenericWebApplicationContext context = new GenericWebApplicationContext(beanFactory);
		context.setParent(parent);
		context.setServletContext(servletContext);
		context.setClassLoader(classLoader);
		
		return context;
	}


}
