package net.java.impala.spring.classloader;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CustomURLClassLoader extends URLClassLoader {

	private Log log = LogFactory.getLog(CustomURLClassLoader.class);

	private Map<String, Class> loadedClasses = new ConcurrentHashMap<String, Class>();

	public CustomURLClassLoader(File location) throws MalformedURLException {

		super(new URL[] { location.toURI().toURL() }, CustomURLClassLoader.class.getClassLoader());
	}

	public CustomURLClassLoader(ClassLoader parent, File location) throws MalformedURLException {
		super(new URL[] { location.toURI().toURL() }, parent);
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {

		try {
			Class<?> parentClass = getParent().loadClass(className);

			if (log.isDebugEnabled())
				log.debug("Returning system class: " + parentClass);

			return parentClass;
		}
		catch (Exception e) {
		}

		try {

			Class loadedClass = loadedClasses.get(className);
			if (loadedClass != null) {
				if (log.isDebugEnabled())
					log.debug("Returning already loaded custom class: " + className);

				return loadedClass;
			}

			Class result = findClass(className);

			loadedClasses.put(className, result);

			return result;

		}
		catch (Exception e) {
		}

		if (log.isDebugEnabled())
			log.debug("Class not found: " + className);
		throw new ClassNotFoundException(className);

	}
}