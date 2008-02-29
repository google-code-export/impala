package org.impalaframework.classloader;

import java.io.File;
import java.net.URL;

public class ModuleClassLoaderFactory implements ClassLoaderFactory {

	public ClassLoader newClassLoader(ClassLoader parent, File[] files) {
		return new ModuleClassLoader(parent, files);
	}

	public ClassLoader newClassLoader(ClassLoader parent, URL[] urls) {
		return new ModuleClassLoader(parent, urls);
	}
}
