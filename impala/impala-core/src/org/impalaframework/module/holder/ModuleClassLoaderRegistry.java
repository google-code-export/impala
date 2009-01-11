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

package org.impalaframework.module.holder;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.spi.ClassLoaderRegistry;

/**
 * Holds a mapping of module names to {@link ClassLoader} instances.
 * @author Phil Zoio
 */
public class ModuleClassLoaderRegistry implements ClassLoaderRegistry {
	
	private ClassLoader applicationClassLoader;
	
	private Map<String,ClassLoader> classLoaders = new HashMap<String, ClassLoader>();
	
	public ClassLoader getApplicationClassLoader() {
		return applicationClassLoader;
	}

	public void setApplicationClassLoader(ClassLoader parentClassLoader) {
		this.applicationClassLoader = parentClassLoader;
	}

	public ClassLoader getClassLoader(String moduleName) {
		return classLoaders.get(moduleName);
	}
	
	public void addClassLoader(String moduleName, ClassLoader classLoader) {
		synchronized (classLoaders) {
			if (classLoaders.containsKey(moduleName)) {
				
				throw new InvalidStateException("Class loader registry already contains class loader for module '" + moduleName + "'");
			}
			classLoaders.put(moduleName, classLoader);
		}
	}
	
	public ClassLoader removeClassLoader(String moduleName) {
		synchronized (classLoaders) {
			return classLoaders.remove(moduleName);
		}
	}

}
