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

package net.java.impala.classloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.java.impala.util.FileUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class FileSystemClassLoader extends ClassLoader {

	private Log log = LogFactory.getLog(FileSystemClassLoader.class);

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

					if (log.isDebugEnabled())
						debug("Returning class newly loaded from custom location: " + className);

					loadedClasses.put(className, result);
					
						System.out.println("ParentClassLoader: " + className + " loaded by "
						 + result.getClassLoader());

					return result;
				}
			}
			return null;

		}
		catch (IOException e) {
			log.error("IOException attempting to read class " + className + " from location(s) "
					+ Arrays.toString(locations));
			return null;
		}
		catch (ClassFormatError e) {
			log.error("Invalid format for class " + className + " from location(s) " + Arrays.toString(locations));
			return null;
		}
	}

	protected Class getAlreadyLoadedClass(String className) {
		Class loadedClass = loadedClasses.get(className);
		if (loadedClass != null) {
			if (log.isDebugEnabled()) {
				debug("Returning already loaded custom class: " + className);
			}
			return loadedClass;
		}
		return loadedClass;
	}

	protected Class<?> loadParentClass(String className) {
		try {
			Class<?> parentClass = getParent().loadClass(className);

			if (log.isDebugEnabled()) {
				debug("Returning from parent class loader " + getParent() + ": " + parentClass);
			}

			return parentClass;
		}
		catch (Exception e) {
		}
		return null;
	}

	@Override
	public URL getResource(String name) {

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
			log.error("IOException attempting to load resource " + name + " from location(s) "
					+ Arrays.toString(locations));
		}

		return super.getResource(name);
	}

	public Map<String, Class> getLoadedClasses() {
		return Collections.unmodifiableMap(loadedClasses);
	}

	private void debug(String message) {
		log.debug(this.getClass().getSimpleName() + "[" + System.identityHashCode(this) + "]: " + message);
	}

}