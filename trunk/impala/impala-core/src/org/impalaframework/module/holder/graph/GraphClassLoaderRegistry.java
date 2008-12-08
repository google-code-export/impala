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

package org.impalaframework.module.holder.graph;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.classloader.graph.GraphClassLoader;
import org.impalaframework.exception.InvalidStateException;

/**
 * Holds a mapping of module names to {@link GraphClassLoader} instances.
 * @author Phil Zoio
 */
public class GraphClassLoaderRegistry {
	
	private ClassLoader parentClassLoader;
	
	private Map<String,GraphClassLoader> graphClassLoaders = new HashMap<String, GraphClassLoader>();
	
	public ClassLoader getParentClassLoader() {
		return parentClassLoader;
	}

	public void setParentClassLoader(ClassLoader parentClassLoader) {
		this.parentClassLoader = parentClassLoader;
	}

	public GraphClassLoader getClassLoader(String moduleName) {
		return graphClassLoaders.get(moduleName);
	}
	
	public void addClassLoader(String moduleName, GraphClassLoader graphClassLoader) {
		synchronized (graphClassLoaders) {
			if (graphClassLoaders.containsKey(moduleName)) {
				
				throw new InvalidStateException("Class loader registry already contains class loader for module '" + moduleName + "'");
			}
			graphClassLoaders.put(moduleName, graphClassLoader);
		}
	}
	
	public GraphClassLoader removeClassLoader(String moduleName) {
		synchronized (graphClassLoaders) {
			return graphClassLoaders.remove(moduleName);
		}
	}

}
