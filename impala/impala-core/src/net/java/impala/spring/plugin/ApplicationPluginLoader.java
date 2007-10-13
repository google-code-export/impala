package net.java.impala.spring.plugin;

import java.io.File;

import net.java.impala.classloader.CustomClassLoader;
import net.java.impala.location.ClassLocationResolver;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class ApplicationPluginLoader implements PluginLoader {

	private ClassLocationResolver classLocationResolver;
	
	public ApplicationPluginLoader(ClassLocationResolver classLocationResolver) {
		super();
		this.classLocationResolver = classLocationResolver;
	}

	public ClassLoader getClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		ClassLoader parentClassLoader = null;
		final PluginSpec parent = pluginSpec.getParent();
		if (parent != null) {
			final ConfigurableApplicationContext parentContext = contextSet.getPluginContext().get(parent.getName());
			if (parentContext != null) {
				parentClassLoader = parentContext.getClassLoader();
			}
		}
		if (parentClassLoader == null) {
			parentClassLoader = ClassUtils.getDefaultClassLoader();
		}
		File[] parentClassLocations = classLocationResolver.getApplicationPluginClassLocations(pluginSpec.getName());
		CustomClassLoader cl = new CustomClassLoader(parentClassLoader, parentClassLocations);
		return cl;
	}

	public Resource[] getClassLocations(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		// TODO Auto-generated method stub
		return null;
	}

	public Resource[] getSpringConfigResources(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		// TODO Auto-generated method stub
		return null;
	}

}
