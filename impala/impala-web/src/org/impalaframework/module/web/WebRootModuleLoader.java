package org.impalaframework.module.web;

import java.io.File;

import javax.servlet.ServletContext;

import org.impalaframework.classloader.FileSystemModuleClassLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.loader.BaseModuleLoader;
import org.impalaframework.module.loader.ModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.spring.web.WebResourceUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class WebRootModuleLoader extends BaseModuleLoader implements ModuleLoader, ServletContextAware {

	private ServletContext servletContext;

	private ModuleLocationResolver moduleLocationResolver;

	public WebRootModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
	}
	
	public WebRootModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		Assert.notNull(servletContext, "ServletContext cannot be null");
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
		this.servletContext = servletContext;
	}

	public GenericWebApplicationContext newApplicationContext(ApplicationContext parent, ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		final GenericWebApplicationContext context = new GenericWebApplicationContext(beanFactory);
		context.setParent(parent);
		context.setServletContext(servletContext);
		context.setClassLoader(classLoader);

		return context;
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		File[] parentClassLocations = getPluginClassLocations(moduleDefinition);
		return new FileSystemModuleClassLoader(ClassUtils.getDefaultClassLoader(), parentClassLocations);
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		return ResourceUtils.getResources(getPluginClassLocations(moduleDefinition));
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		return WebResourceUtils.getServletContextResources(moduleDefinition.getContextLocations(), servletContext);
	}

	private File[] getPluginClassLocations(ModuleDefinition moduleDefinition) {
		File[] parentClassLocations = moduleLocationResolver.getApplicationPluginClassLocations(moduleDefinition.getName());
		return parentClassLocations;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	protected ModuleLocationResolver getClassLocationResolver() {
		return moduleLocationResolver;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}

}
