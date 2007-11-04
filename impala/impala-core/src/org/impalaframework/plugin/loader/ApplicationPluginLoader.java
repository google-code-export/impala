package org.impalaframework.plugin.loader;

import java.io.File;


import org.impalaframework.classloader.ParentClassLoader;
import org.impalaframework.plugin.plugin.ApplicationContextSet;
import org.impalaframework.plugin.plugin.PluginSpec;
import org.impalaframework.plugin.plugin.PluginUtils;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class ApplicationPluginLoader extends BasePluginLoader implements PluginLoader {

	private ClassLocationResolver classLocationResolver;

	public ApplicationPluginLoader(ClassLocationResolver classLocationResolver) {
		super();
		Assert.notNull("classLocationResolver cannot be null");
		this.classLocationResolver = classLocationResolver;
	}

	public GenericApplicationContext newApplicationContext(ApplicationContext parent, ClassLoader classLoader) {
		Assert.notNull(parent, "parent cannot be null");
		Assert.notNull(classLoader, "classloader cannot be null");
		
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		// create the application context, and set the class loader
		GenericApplicationContext context = new GenericApplicationContext(beanFactory, parent);
		context.setClassLoader(classLoader);
		return context;
	}

	public ClassLoader newClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec, ApplicationContext parent) {
		ClassLoader parentClassLoader = PluginUtils.getParentClassLoader(parent);
		File[] classLocations = classLocationResolver.getApplicationPluginClassLocations(pluginSpec.getName());
		ParentClassLoader cl = new ParentClassLoader(parentClassLoader, classLocations);
		return cl;
	}

	public Resource[] getClassLocations(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		File[] classLocations = classLocationResolver.getApplicationPluginClassLocations(pluginSpec.getName());
		return ResourceUtils.getResources(classLocations);
	}

	public Resource[] getSpringConfigResources(ApplicationContextSet contextSet, PluginSpec pluginSpec,
			ClassLoader classLoader) {
		File springLocation = this.classLocationResolver.getApplicationPluginSpringLocation(pluginSpec.getName());
		return new Resource[] { new FileSystemResource(springLocation) };
	}

}
