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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.classloader.ModularClassLoader;
import org.impalaframework.module.ModuleDefinition;

/**
 * Class loader backed by a graph of dependent class loaders. Each module will
 * have one of these. Includes a mechanism which delegates to first to the class
 * loaders of dependent modules, and only uses the local resource class loader
 * if this unsuccessful.
 * 
 * @author Phil Zoio
 */
public class GraphClassLoader extends ClassLoader implements ModularClassLoader {

	private static final Log logger = LogFactory.getLog(GraphClassLoader.class);

	private Map<String, Class<?>> loadedClasses = new ConcurrentHashMap<String, Class<?>>();
	
	private ModuleDefinition moduleDefinition;
	private ClassRetriever classRetriever;
	private DelegateClassLoader delegateClassLoader;
	private boolean loadParentFirst;
	
	public GraphClassLoader(
			ClassLoader parentClassLoader,
			DelegateClassLoader delegateClassLoader,
			ClassRetriever classRetriever, 
			ModuleDefinition definition, boolean loadParentFirst) {
		
		super(parentClassLoader);
		this.moduleDefinition = definition;
		this.classRetriever = classRetriever;
		this.delegateClassLoader = delegateClassLoader;
		this.loadParentFirst = loadParentFirst;		
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entering loading class '" + className + "' from " + this);
		}
		
		Class<?> loadClass = null; 
		
		if (logger.isTraceEnabled()) {
			logger.trace("For class loader, load parent first " + loadParentFirst);
		}
		
		if (!loadParentFirst) {
			if (loadClass == null) {
				loadClass = loadCustomClass(className, true);
			}
		}
		
		if (loadClass == null) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Delegating to parent class loader to load " + className);
				}
				loadClass = getParent().loadClass(className);
			} catch (ClassNotFoundException e) {
			}
		}

		if (loadParentFirst) {
			if (loadClass == null) {
				loadClass = loadCustomClass(className, true);
			}
		}
		
		if (loadClass != null) {
			return loadClass;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Unable to find class " + className);
			logger.debug("Using class loader: " + this);
		}
		
		throw new ClassNotFoundException("Unable to find class " + className);
	}

	public Class<?> loadCustomClass(String className, boolean tryDelegate) throws ClassNotFoundException,
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
			byte[] bytes = classRetriever.getClassBytes(className);
			if (bytes != null) {
				
				if (logger.isDebugEnabled()) {
					logger.debug("Found bytes for '" + className + "' from " + this);
				}
				
				//bytes found - define class
				clazz = defineClass(className, bytes, 0, bytes.length, null);
				loadedClasses.put(className, clazz);

				logger.info("Class '" + className + "' found using class loader for " + this.getModuleName());
			}
		}
		
		return clazz;
	}

	public boolean hasVisibilityOf(ClassLoader classLoader){
		if (classLoader == this) {
			return true;
		}
		return delegateClassLoader.hasVisibilityOf(classLoader);
	}

	
	/**
	 * Attempt to load a resource, first by calling
	 * <code>getCustomResource</code>. If the resource is not found
	 * <code>super.getResource(name)</code> is called.
	 */
	@Override
	public URL getResource(String name) {
		
		URL url = getLocalResource(name);
		if (url != null) {
			return url;
		}
		
		url = delegateClassLoader.getResource(name);
		if (url != null) {
			return url;
		}
		
		return super.getResource(name);
	}
	
	/**
	 * Returns enumeration of local resource, combined with those of parent
	 * class loader.
	 */
	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		Enumeration<URL> resources = super.getResources(name);
		
		URL localResource = getLocalResource(name);
		if (localResource != null) {
			List<URL> combined = new ArrayList<URL>();
			ArrayList<URL> list = Collections.list(resources);
			combined.add(localResource);
			combined.addAll(list);
			return Collections.enumeration(combined);
		}
		
		return resources;
	}

	/**
	 * Attempts to find a resource from one of the file system locations
	 * specified in a constructor.
	 * @param name the name of the resource to load
	 * @return a <code>URL</code> instance, if the resource can be found,
	 * otherwise null.
	 */
	protected URL getLocalResource(String name) {
		return classRetriever.findResource(name);
	}
	
	Map<String, Class<?>> getLoadedClasses() {
		return Collections.unmodifiableMap(loadedClasses);
	}

	protected final String getModuleName() {
		return moduleDefinition.getName();
	}
	
	@Override
	public String toString() {
		String lineSeparator = System.getProperty("line.separator");
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Class loader for " + moduleDefinition.getName()).append(lineSeparator);
		stringBuffer.append("Loading first from parent: " + loadParentFirst).append(lineSeparator);
		stringBuffer.append(delegateClassLoader);
		
		return stringBuffer.toString();
	}
	
}
