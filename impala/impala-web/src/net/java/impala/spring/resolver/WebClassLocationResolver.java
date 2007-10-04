package net.java.impala.spring.resolver;

import java.io.File;

import net.java.impala.location.ClassLocationResolver;

public interface WebClassLocationResolver extends ClassLocationResolver {
	
	/**
	 * Returns the directory location for Spring context files for a particular plugin
	 * @param parentName the name of the parent application prefix. Used to
	 * resolve plugin names
	 * @param servletName the name of the servlet associated with the plugin
	 */
	public File getParentWebClassLocation(String parentName, String servletName);

}
