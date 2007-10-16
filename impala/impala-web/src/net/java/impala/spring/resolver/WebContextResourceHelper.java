package net.java.impala.spring.resolver;

import net.java.impala.classloader.ContextResourceHelper;

@Deprecated
public interface WebContextResourceHelper extends ContextResourceHelper {
	ClassLoader getWebClassLoader(String pluginName);
}
