package org.impalaframework.spring.classloader;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomURLClassLoader extends URLClassLoader {

	final Logger logger = LoggerFactory.getLogger(CustomURLClassLoader.class);

	private Map<String, Class<?>> loadedClasses = new ConcurrentHashMap<String, Class<?>>();

	public CustomURLClassLoader(File location) throws MalformedURLException {

		super(new URL[] { location.toURI().toURL() }, CustomURLClassLoader.class.getClassLoader());
	}

	public CustomURLClassLoader(ClassLoader parent, File location) throws MalformedURLException {
		super(new URL[] { location.toURI().toURL() }, parent);
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {

		try {
			Class<?> parentClass = getParent().loadClass(className);

			if (logger.isDebugEnabled())
				logger.debug("Returning system class: " + parentClass);

			return parentClass;
		}
		catch (Exception e) {
		}

		try {

			Class<?> loadedClass = loadedClasses.get(className);
			if (loadedClass != null) {
				if (logger.isDebugEnabled())
					logger.debug("Returning already loaded custom class: " + className);

				return loadedClass;
			}

			Class<?> result = findClass(className);

			loadedClasses.put(className, result);

			return result;

		}
		catch (Exception e) {
		}

		if (logger.isDebugEnabled())
			logger.debug("Class not found: " + className);
		throw new ClassNotFoundException(className);

	}
}