/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.classloader.graph;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.util.ClassUtils;

/**
 * Classloader backed by a graph of dependent class loaders. Each module will have one of these.
 * Includes a mechanism which 
 * @author Phil Zoio
 */
public class GraphClassLoader extends ClassLoader {

	private static final Log logger = LogFactory.getLog(GraphClassLoader.class);

	private Map<String, Class<?>> loadedClasses = new ConcurrentHashMap<String, Class<?>>();
	
	private ModuleDefinition moduleDefinition;
	private CustomClassLoader resourceLoader;
	private DelegateClassLoader delegateClassLoader;
	private ClassLoader parent;
	
	public GraphClassLoader(
			DelegateClassLoader delegateClassLoader,
			CustomClassLoader resourceLoader,
			ModuleDefinition definition) {
		super();
		this.moduleDefinition = definition;
		this.resourceLoader = resourceLoader;
		this.delegateClassLoader = delegateClassLoader;
		this.parent = ClassUtils.getDefaultClassLoader();
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		
		//TODO add option of loading parent class first
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entering loading class '" + className + "' from " + this);
		}
		
		Class<?> loadClass = loadClass(className, true);
		
		if (loadClass == null) {
			return parent.loadClass(className);
		}
		
		if (loadClass != null)
			return loadClass;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Unable to find class " + className);
			logger.debug("Using class loader: " + this);
		}
		
		throw new ClassNotFoundException("Unable to find class " + className);
	}

	public Class<?> loadClass(String className, boolean tryDelegate) throws ClassNotFoundException,
			ClassFormatError {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Loading class '" + className + "' from " + this);
		}
		
		final Class<?> alreadyLoaded = loadedClasses.get(className);
		
		if (alreadyLoaded != null) {
			
			if (logger.isDebugEnabled()) {
				logger.debug("Returning already loaded class for '" + className + "' from " + this);
			}
			return alreadyLoaded;
		}
		
		//first try the delegate, so that the class loaders for modules higher in the dependency
		//chain can be tried first.
		Class<?> clazz = null;
		
		if (tryDelegate) {
			clazz = delegateClassLoader.loadClass(className);
		}
		
		if (clazz == null) {
			try {
				byte[] bytes = resourceLoader.findClassBytes(className);
				if (bytes != null) {
					
					if (logger.isDebugEnabled()) {
						logger.debug("Found bytes for '" + className + "' from " + this);
					}
					
					//bytes found - define class
					clazz = defineClass(className, bytes, 0, bytes.length, null);
					loadedClasses.put(className, clazz);
				}
			} catch (IOException e) {
			}
		}
		
		return clazz;
	}
	
	Map<String, Class<?>> getLoadedClasses() {
		return Collections.unmodifiableMap(loadedClasses);
	}

	@Override
	public String toString() {
		//TODO enhance this implementation. Need really detailed toString method for better debugging
		return new StringBuffer("Class loader for " + moduleDefinition.getName()).toString();
	}
	
}
