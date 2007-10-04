package net.java.impala.classloader;

import org.springframework.core.io.Resource;


public interface ContextResourceHelper {

	CustomClassLoader getPluginClassLoader(ClassLoader parent, String plugin);
	ClassLoader getParentClassLoader(ClassLoader existing, String plugin);
	Resource getPluginSpringLocation(String plugin);

}
