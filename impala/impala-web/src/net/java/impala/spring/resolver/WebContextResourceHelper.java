package net.java.impala.spring.resolver;

import net.java.impala.classloader.ContextResourceHelper;

public interface WebContextResourceHelper extends ContextResourceHelper {
	ClassLoader getWebClassLoader(String parentName, String servletName);
}
