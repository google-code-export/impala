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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classloader backed by a graph of dependent class loaders. Idea is that each module will have one of these
 * @author Phil Zoio
 */
public class GraphBasedClassLoader extends ClassLoader {

	private static final Log logger = LogFactory.getLog(GraphBasedClassLoader.class);
	
	private List<CustomClassLoader> classLoaders;

	private Map<String, Class<?>> loadedClasses = new ConcurrentHashMap<String, Class<?>>();
	
	public GraphBasedClassLoader(DependencyRegistryClassLoaderHelper registry, String name) {
		super();
		classLoaders = registry.getLoadersFor(name);
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		
		System.out.println("Loading class: " + className);
		
		final Class<?> alreadyLoaded = loadedClasses.get(className);
		if (alreadyLoaded != null) {
			return alreadyLoaded;
		}
		
		for (CustomClassLoader customClassLoader : classLoaders) {

			Class<?> result = null;
			
			byte[] classData = null;
			try {

				//FIXME should attempt to read from Resource instance, rather than using CustomClassLoader
				
				classData = customClassLoader.findClassBytes(className);
			} catch (IOException e) {
				e.printStackTrace();
				//FIXME do something about this
			}
			
			if (classData != null) {
				result = defineClass(className, classData, 0, classData.length, null);
	
				if (logger.isDebugEnabled())
					logger.debug("Returning class newly loaded from custom location: " + className);
	
				loadedClasses.put(className, result);
	
				logger.info("ModuleClassLoader: " + className + " loaded by " + result.getClassLoader());
			}
			
			if (result != null) {
				return result;
			}
		}

		return super.loadClass(className);
	}

	@Override
	public String toString() {
		final String string = super.toString();
		StringBuffer buffer = new StringBuffer(string);
		buffer.append("Module class loaders ");
		
		List<CustomClassLoader> classLoaders = this.classLoaders;
		for (CustomClassLoader customClassLoader : classLoaders) {
			buffer.append("\n").append(customClassLoader.toString());
		}
		return buffer.toString();
	}
	
}
