package org.impalaframework.module.holder.graph;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.classloader.graph.GraphClassLoader;

public class GraphClassLoaderRegistry {
	
	private Map<String,GraphClassLoader> graphClassLoaders = new HashMap<String, GraphClassLoader>();
	
	public GraphClassLoader getClassLoader(String moduleName) {
		return graphClassLoaders.get(moduleName);
	}
	
	public void addClassLoader(String moduleName, GraphClassLoader graphClassLoader) {
		synchronized (graphClassLoaders) {
			if (graphClassLoaders.containsKey(moduleName)) {
				//FIXME at least log a warning here, possibly throw exception
				throw new IllegalStateException("Already contains class loader for module " + moduleName);
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
