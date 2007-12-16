/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.classloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class FileSystemClassLoader extends ClassLoader {

	final Logger logger = LoggerFactory.getLogger(FileSystemClassLoader.class);

	private File[] locations;

	private Map<String, Class> loadedClasses = new ConcurrentHashMap<String, Class>();

	public FileSystemClassLoader(File[] locations) {
		super(ClassUtils.getDefaultClassLoader());
		this.locations = locations;
	}

	public FileSystemClassLoader(ClassLoader parent, File[] locations) {
		super(parent);
		this.locations = locations;
	}

	protected Class loadCustomClass(String className) {

		String relativeClassFile = className.replace('.', '/') + ".class";

		try {
			for (int i = 0; i < locations.length; i++) {

				File file = new File(locations[i], relativeClassFile);
				if (file.exists()) {

					byte[] classData = FileUtils.getBytes(file);

					Class result = defineClass(className, classData, 0, classData.length, null);

					if (logger.isDebugEnabled())
						debug("Returning class newly loaded from custom location: {}" + className);

					loadedClasses.put(className, result);

					logger.info("FileSystemModuleClassLoader: {} loaded by {}", className, result.getClassLoader());

					return result;
				}
			}
			return null;

		}
		catch (IOException e) {
			logger.error("IOException attempting to read class {} from location(s) {}", className, Arrays
					.toString(locations));
			return null;
		}
		catch (ClassFormatError e) {
			logger.error("Invalid format for class {} from location(s) ", className, Arrays.toString(locations));
			return null;
		}
	}

	protected Class getAlreadyLoadedClass(String className) {
		Class loadedClass = loadedClasses.get(className);
		if (loadedClass != null) {
			if (logger.isDebugEnabled()) {
				debug("Returning already loaded custom class: " + className);
			}
			return loadedClass;
		}
		return loadedClass;
	}

	protected Class<?> loadParentClass(String className) {
		try {
			Class<?> parentClass = getParent().loadClass(className);

			if (logger.isDebugEnabled()) {
				debug("Returning from parent class loader {}: {}" + getParent() + ": " + parentClass);
			}

			return parentClass;
		}
		catch (Exception e) {
		}
		return null;
	}

	@Override
	public URL getResource(String name) {

		final URL url = getCustomResource(name);
		if (url != null) {
			return url;
		}

		return super.getResource(name);
	}

	protected URL getCustomResource(String name) {
		try {

			for (int i = 0; i < locations.length; i++) {
				File file = new File(locations[i], name);
				if (file.exists()) {
					URL url = file.toURI().toURL();
					return url;
				}
			}

		}
		catch (IOException e) {
			logger.error("IOException attempting to load resource {} from location(s) ", name, Arrays
					.toString(locations));
		}
		return null;
	}

	public Map<String, Class> getLoadedClasses() {
		return Collections.unmodifiableMap(loadedClasses);
	}

	private void debug(String message) {
		logger.debug(this.getClass().getSimpleName() + "[" + System.identityHashCode(this) + "]: " + message);
	}

}