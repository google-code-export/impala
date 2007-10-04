package net.java.impala.classloader;

import net.java.impala.classloader.ContextResourceHelper;

public interface TestContextResourceHelper extends ContextResourceHelper {

	public ClassLoader getTestClassLoader(ClassLoader parentClassLoader, String name);
}
