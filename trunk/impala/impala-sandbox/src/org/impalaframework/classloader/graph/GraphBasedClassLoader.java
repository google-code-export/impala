package org.impalaframework.classloader.graph;

import java.util.List;

public class GraphBasedClassLoader extends ClassLoader {
	
	private List<CustomClassLoader> classLoaders;
	
	public GraphBasedClassLoader(DependencyRegistry registry, String name) {
		super();
		classLoaders = registry.getLoadersFor(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		
		System.out.println("Loading class: " + name);
		
		for (CustomClassLoader customClassLoader : classLoaders) {
			final Class<?> loadCustomClass = customClassLoader.loadCustomClass(name);
			if (loadCustomClass != null) {
				return loadCustomClass;
			}
		}

		return super.loadClass(name);
	}

	
	
}
