package org.impalaframework.classloader;

import java.io.File;
import java.net.URL;

public interface ClassLoaderFactory {
	public ClassLoader newClassLoader(ClassLoader parent, File[] files);
	public ClassLoader newClassLoader(ClassLoader parent, URL[] urls);
}
