package org.impalaframework.spring.classloader;


import java.io.File;
import java.net.MalformedURLException;



public class CustomClassLoaderFactory implements ClassLoaderFactory {

	private File location;

	public ClassLoader getClassLoader() {
		if (location == null)
			throw new IllegalStateException("Location not specified");
		if (!location.exists())
			throw new IllegalStateException("Location " + location + " does not exist");
		if (!location.isDirectory())
			throw new IllegalStateException("Location is not a directory");

		CustomURLClassLoader customClassLoader;
		try {
			customClassLoader = new CustomURLClassLoader(location);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return customClassLoader;
	}

	public void setLocation(File location) {
		this.location = location;
	}

}
