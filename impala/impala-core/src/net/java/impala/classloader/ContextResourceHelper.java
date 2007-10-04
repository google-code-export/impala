package net.java.impala.classloader;

import org.springframework.core.io.Resource;


public interface ContextResourceHelper {

	CustomClassLoader getApplicationPluginClassLoader(ClassLoader parent, String plugin);
	ClassLoader getParentClassLoader(ClassLoader existing, String plugin);
	Resource getApplicationPluginSpringLocation(String plugin);

}
