package org.impalaframework.classloader;

import java.io.File;
import java.net.URL;

public class ParentClassLoaderFactory implements ClassLoaderFactory {

	public ClassLoader newClassLoader(ClassLoader parent, File[] files) {
		return new ParentClassLoader(parent, files);
	}

	public ClassLoader newClassLoader(ClassLoader parent, URL[] urls) {
		return new ParentClassLoader(parent, urls);
	}
}
