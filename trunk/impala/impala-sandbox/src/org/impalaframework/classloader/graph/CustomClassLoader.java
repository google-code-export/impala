package org.impalaframework.classloader.graph;

import java.io.File;

public class CustomClassLoader extends org.impalaframework.classloader.CustomClassLoader {

	public CustomClassLoader(ClassLoader parent, File[] locations) {
		super(parent, locations);
	}

	public CustomClassLoader(File[] locations) {
		super(locations);
	}

	@Override
	public Class<?> loadCustomClass(String className) {
		return super.loadCustomClass(className);
	}

	
	
}
