package org.impalaframework.classloader.graph;

import java.io.File;
import java.io.IOException;

public class CustomClassLoader extends org.impalaframework.classloader.FileSystemClassLoader {

	public CustomClassLoader(ClassLoader parent, File[] locations) {
		super(parent, locations);
	}

	public CustomClassLoader(File[] locations) {
		super(locations);
	}

	@Override
	public byte[] findClassBytes(String className) throws IOException {
		return super.findClassBytes(className);
	}
	
}
