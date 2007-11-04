package org.impalaframework.plugin.loader;

import java.io.File;


import org.impalaframework.classloader.ParentClassLoader;
import org.impalaframework.plugin.plugin.ApplicationContextSet;
import org.impalaframework.plugin.plugin.PluginSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class ParentPluginLoader extends BasePluginLoader implements PluginLoader {

	private ClassLocationResolver classLocationResolver;

	public ParentPluginLoader(ClassLocationResolver classLocationResolver) {
		super();
		Assert.notNull("classLocationResolver cannot be null");
		this.classLocationResolver = classLocationResolver;
	}

	public GenericApplicationContext newApplicationContext(ApplicationContext parent, ClassLoader classLoader) {
		Assert.isNull(parent, "parent must be null");
		Assert.notNull(classLoader, "classloader cannot be null");

		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		GenericApplicationContext context = new GenericApplicationContext(beanFactory);
		context.setClassLoader(classLoader);
		return context;
	}

	public ClassLoader newClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec, ApplicationContext parent) {
		File[] parentClassLocations = getParentClassLocations();
		return new ParentClassLoader(ClassUtils.getDefaultClassLoader(), parentClassLocations);
	}

	public Resource[] getClassLocations(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		return ResourceUtils.getResources(getParentClassLocations());
	}

	private File[] getParentClassLocations() {
		String parentProject = classLocationResolver.getParentProject();
		File[] parentClassLocations = classLocationResolver.getApplicationPluginClassLocations(parentProject);
		return parentClassLocations;
	}

	public Resource[] getSpringConfigResources(ApplicationContextSet contextSet, PluginSpec pluginSpec,
			ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(pluginSpec.getContextLocations(), classLoader);
	}

}
