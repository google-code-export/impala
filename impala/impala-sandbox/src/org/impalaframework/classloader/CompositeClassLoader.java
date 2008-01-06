package org.impalaframework.classloader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public class CompositeClassLoader extends ClassLoader {

	private List<FileSystemClassLoader> classLoaders;

	private ClassLoader parent;

	public CompositeClassLoader(List<FileSystemClassLoader> classLoaders) {
		super();
		Assert.notNull(classLoaders);
		Assert.notEmpty(classLoaders);

		final ClassLoader firstParent = classLoaders.get(0).getParent();
		if (classLoaders.size() > 1) {
			for (int i = 1; i < classLoaders.size(); i++) {
				FileSystemClassLoader cl = classLoaders.get(i);
				Assert.isTrue(
					cl.getParent() == firstParent,
					"All class loaders must share from the same parent. That is, they need to inherit from the same hierarchy. First parent is "
							+ firstParent + " while " + cl + " has parent " + cl.getParent());
			}
		}
		this.classLoaders = new ArrayList<FileSystemClassLoader>(classLoaders);
		this.parent = firstParent;
	}

	@Override
	public URL getResource(String name) {
		for (FileSystemClassLoader classLoader : classLoaders) {
			final URL customResource = classLoader.getCustomResource(name);
			if (customResource != null)
				return customResource;
		}
		return parent.getResource(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		final Class<?> parentClass = this.classLoaders.get(0).loadParentClass(name);

		if (parentClass != null) {
			return parentClass;
		}

		for (FileSystemClassLoader classLoader : classLoaders) {
			final Class<?> alreadyLoadedClass = classLoader.getAlreadyLoadedClass(name);
			if (alreadyLoadedClass != null)
				return alreadyLoadedClass;
		}

		for (FileSystemClassLoader classLoader : classLoaders) {
			final Class<?> customClass = classLoader.loadCustomClass(name);
			if (customClass != null)
				return customClass;
		}

		throw new ClassNotFoundException(name + " cannot be found using class loaders " + classLoaders);
	}

	public void addClassLoader(FileSystemClassLoader loader) {
		this.classLoaders.add(loader);
	}
	
	public boolean removeClassLoader(FileSystemClassLoader loader) {
		return this.classLoaders.remove(loader);
	}

}
