package org.impalaframework.web.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletContext;

import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.loader.BaseModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.spring.resource.ResourceLoader;
import org.impalaframework.util.ResourceUtils;
import org.impalaframework.web.resource.ServletContextResourceLoader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class WebRootModuleLoader extends BaseModuleLoader implements ServletContextAware {

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
		Resource[] parentClassLocations = getModuleClassLocations(moduleDefinition);
		return new ModuleClassLoader(ClassUtils.getDefaultClassLoader(), ResourceUtils.getFiles(parentClassLocations));
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		return getModuleClassLocations(moduleDefinition);
	}

	private Resource[] getModuleClassLocations(ModuleDefinition moduleDefinition) {
		List<Resource> parentClassLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
		return ResourceUtils.toArray(parentClassLocations);
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

	@Override
	protected Collection<ResourceLoader> getSpringLocationResourceLoaders() {
		Collection<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();
		ServletContextResourceLoader servletContextResourceLoader = new ServletContextResourceLoader();
		servletContextResourceLoader.setServletContext(servletContext);
		resourceLoaders.add(servletContextResourceLoader);
		return resourceLoaders;
	}

}
