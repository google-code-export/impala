package net.java.impala.location;

import java.io.File;

public interface ClassLocationResolver {

	/**
	 * Returns the directory locations for parent application context classes
	 */
	public File[] getParentClassLocations(String projectName);

	/**
	 * Returns the directory locations for test classes for a parent project
	 */
	public File[] getTestClassLocations(String projectName);

	/**
	 * Returns the directory locations for plugin classes
	 */
	public File[] getApplicationPluginClassLocations(String plugin);

	/**
	 * Returns the directory location for Spring context files for a particular plugin
	 */
	public File getPluginSpringLocation(String plugin);

}
