package net.java.impala.classloader;

import java.io.File;

import net.java.impala.location.ClassLocationResolver;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class DefaultContextResourceHelper implements ContextResourceHelper {

	private ClassLocationResolver classLocationResolver;

	public DefaultContextResourceHelper(ClassLocationResolver classLocationResolver) {
		super();
		Assert.notNull(classLocationResolver, ClassLocationResolver.class.getSimpleName() + " cannot be null");
		this.classLocationResolver = classLocationResolver;
	}

	public CustomClassLoader getApplicationPluginClassLoader(ClassLoader parent, String plugin) {
		File[] pluginClassLocations = this.classLocationResolver.getPluginClassLocations(plugin);
		return new CustomClassLoader(parent, pluginClassLocations);
	}

	public ClassLoader getParentClassLoader(ClassLoader existing, String plugin) {
		File[] parentClassLocations = classLocationResolver.getParentClassLocations(plugin);
		ParentClassLoader cl = new ParentClassLoader(existing, parentClassLocations);
		return cl;
	}

	public ClassLoader getTestClassLoader(ClassLoader parentClassLoader, File[] locations, String name) {
		throw new UnsupportedOperationException();
	}

	public ClassLocationResolver getClassLocationResolver() {
		return classLocationResolver;
	}

	//FIXME add test
	public Resource getApplicationPluginSpringLocation(String plugin) {
		return new FileSystemResource(classLocationResolver.getPluginSpringLocation(plugin));
	}

}
