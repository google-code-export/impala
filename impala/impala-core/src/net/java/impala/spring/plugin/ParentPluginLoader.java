package net.java.impala.spring.plugin;

import java.io.File;

import net.java.impala.classloader.ParentClassLoader;
import net.java.impala.location.ClassLocationResolver;

import org.springframework.core.io.ClassPathResource;
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

	public Resource[] getSpringConfigResources(ApplicationContextSet contextSet, PluginSpec pluginSpec, ClassLoader classLoader) {
		String[] locations = pluginSpec.getContextLocations();
		Resource[] resources = new Resource[locations.length];

		for (int i = 0; i < locations.length; i++) {
			//note that this is relying on the contextClassLoader to be set up correctly
			resources[i] = new ClassPathResource(locations[i], classLoader);
		}
		return resources;
	}

}
