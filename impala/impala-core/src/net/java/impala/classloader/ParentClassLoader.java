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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Subclass of <code>FileSystemClassLoader</code> which will attempt to load from the supplied
 * class location BEFORE attempting to load using the parent class location
 * @author Phil Zoio
 */
public class ParentClassLoader extends FileSystemClassLoader {

	private Log log = LogFactory.getLog(ParentClassLoader.class);

	public ParentClassLoader(File[] locations) {
		super(locations);
	}

	public ParentClassLoader(ClassLoader parent, File[] locations) {
		super(parent, locations);
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {

		Class<?> toReturn = null;
		if (toReturn == null) {
			toReturn = getAlreadyLoadedClass(className);
		}
		if (toReturn == null) {
			toReturn = loadCustomClass(className);
		}
		if (toReturn == null) {
			toReturn = loadParentClass(className);
		}
		if (toReturn == null) {
			if (log.isDebugEnabled())
				log.debug("Class not found: " + className);
			throw new ClassNotFoundException(className);
		}

		return toReturn;
	}

}