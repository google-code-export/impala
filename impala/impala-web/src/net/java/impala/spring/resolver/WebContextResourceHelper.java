package net.java.impala.spring.resolver;

import org.springframework.core.io.Resource;

import net.java.impala.classloader.ContextResourceHelper;

public interface WebContextResourceHelper extends ContextResourceHelper {
	ClassLoader getWebClassLoader(String parentName, String servletName);

	Resource getParentWebClassLocation(String parentName, String servletName);
}
