package org.impalaframework.plugin.loader;

import java.io.File;

import org.impalaframework.classloader.ParentClassLoader;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.util.ResourceUtils;
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

	@Override
	public GenericApplicationContext newApplicationContext(ApplicationContext parent, PluginSpec pluginSpec, ClassLoader classLoader) {
		Assert.notNull(parent, "parent cannot be null");
		return super.newApplicationContext(parent, pluginSpec, classLoader);
	}

	public ClassLoader newClassLoader(PluginSpec pluginSpec, ApplicationContext parent) {
		ClassLoader parentClassLoader = PluginUtils.getParentClassLoader(parent);
		File[] classLocations = classLocationResolver.getApplicationPluginClassLocations(pluginSpec.getName());
		ParentClassLoader cl = new ParentClassLoader(parentClassLoader, classLocations);
		return cl;
	}

	public Resource[] getClassLocations(PluginSpec pluginSpec) {
		File[] classLocations = classLocationResolver.getApplicationPluginClassLocations(pluginSpec.getName());
		return ResourceUtils.getResources(classLocations);
	}

	public Resource[] getSpringConfigResources(PluginSpec pluginSpec, ClassLoader classLoader) {
		File springLocation = this.classLocationResolver.getApplicationPluginSpringLocation(pluginSpec.getName());
		return new Resource[] { new FileSystemResource(springLocation) };
	}

}
