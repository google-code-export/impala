package net.java.impala.spring.plugin;

import java.io.File;

import net.java.impala.classloader.ParentClassLoader;
import net.java.impala.location.ClassLocationResolver;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class ParentPluginLoader implements PluginLoader {

	private ClassLocationResolver classLocationResolver;
	
	public ParentPluginLoader(ClassLocationResolver classLocationResolver) {
		super();
		Assert.notNull("classLocationResolver cannot be null");
		this.classLocationResolver = classLocationResolver;
	}
	
	public ClassLoader newClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		File[] parentClassLocations = classLocationResolver.getApplicationPluginClassLocations(pluginSpec.getName());
		return new ParentClassLoader(ClassUtils.getDefaultClassLoader(), parentClassLocations);
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
