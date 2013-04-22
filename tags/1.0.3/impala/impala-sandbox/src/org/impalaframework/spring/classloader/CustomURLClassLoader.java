/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.spring.classloader;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CustomURLClassLoader extends URLClassLoader {

	private static final Log logger = LogFactory.getLog(CustomURLClassLoader.class);

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