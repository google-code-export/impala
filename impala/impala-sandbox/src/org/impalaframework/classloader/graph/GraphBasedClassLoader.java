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
	
	public GraphBasedClassLoader(DependencyRegistry registry, String name) {
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

	
	
}
