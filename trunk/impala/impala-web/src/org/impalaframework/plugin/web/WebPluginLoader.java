package org.impalaframework.plugin.web;

import java.io.File;

import javax.servlet.ServletContext;

import org.impalaframework.classloader.ParentClassLoader;
import org.impalaframework.plugin.loader.BasePluginLoader;
import org.impalaframework.plugin.loader.PluginLoader;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.spring.web.WebResourceUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class WebPluginLoader extends BasePluginLoader implements PluginLoader, ServletContextAware {

	private ServletContext servletContext;

	private ClassLocationResolver classLocationResolver;

	public WebPluginLoader(ClassLocationResolver classLocationResolver) {
		//FIXME test with setServletContextLoader
		Assert.notNull(classLocationResolver, "classLocationResolver cannot be null");
		this.classLocationResolver = classLocationResolver;
	}
	
	public WebPluginLoader(ClassLocationResolver classLocationResolver, ServletContext servletContext) {
		Assert.notNull(servletContext, "ServletContext cannot be null");
		Assert.notNull(classLocationResolver, "classLocationResolver cannot be null");
		this.classLocationResolver = classLocationResolver;
		this.servletContext = servletContext;
	}

	public GenericWebApplicationContext newApplicationContext(ApplicationContext parent, PluginSpec pluginSpec, ClassLoader classLoader) {
		final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		final GenericWebApplicationContext context = new GenericWebApplicationContext(beanFactory);
		context.setParent(parent);
		context.setServletContext(servletContext);
		context.setClassLoader(classLoader);

		return context;
	}

	public ClassLoader newClassLoader(PluginSpec pluginSpec, ApplicationContext parent) {
		File[] parentClassLocations = getPluginClassLocations(pluginSpec);
		return new ParentClassLoader(ClassUtils.getDefaultClassLoader(), parentClassLocations);
	}

	public Resource[] getClassLocations(PluginSpec pluginSpec) {
		return ResourceUtils.getResources(getPluginClassLocations(pluginSpec));
	}

	public Resource[] getSpringConfigResources(PluginSpec pluginSpec, ClassLoader classLoader) {
		return WebResourceUtils.getServletContextResources(pluginSpec.getContextLocations(), servletContext);
	}

	private File[] getPluginClassLocations(PluginSpec pluginSpec) {
		File[] parentClassLocations = classLocationResolver.getApplicationPluginClassLocations(pluginSpec.getName());
		return parentClassLocations;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
