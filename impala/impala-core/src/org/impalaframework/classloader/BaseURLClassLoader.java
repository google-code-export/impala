/*
 * Copyright 2007-2010 the original author or authors.
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
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.util.URLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link ClassLoader} which resolves a particular class or resource from one of
 * a set of locations on the file system. Note that this class is abstract as it
 * does not override the superclass's <code>loadClass()</code> method.
 * 
 * @author Phil Zoio
 */
public abstract class BaseURLClassLoader extends java.net.URLClassLoader {

    private static final Log logger = LogFactory.getLog(BaseURLClassLoader.class);
    
    private Map<String, Class<?>> loadedClasses = new ConcurrentHashMap<String, Class<?>>();
    
    private URL[] urls;

    /**
     * Constructs this class loader with a set of <code>File</code> locations
     * from which the class can be be loaded
     */
    public BaseURLClassLoader(File[] locations) {
        this(URLUtils.createUrls(locations));
    }

    /**
     * As with the overloaded constructor, except that it provides a parent
     * <code>ClassLoader</code>.
     */
    public BaseURLClassLoader(ClassLoader parent, File[] locations) {
        this(parent, URLUtils.createUrls(locations));
    }
    
    /**
     * Constructs this class loader with a set of <code>URL</code> locations
     * from which the class can be be loaded
     */
    public BaseURLClassLoader(URL[] locations) {
        super(locations);
        this.urls = locations;
    }

    /**
     * As with the overloaded constructor, except that it provides a parent
     * <code>ClassLoader</code>.
     */
    public BaseURLClassLoader(ClassLoader parent, URL[] locations) {
        super(locations, parent);
        this.urls = locations;
    }

    /**
     * Attempts to load class from one of the locations supplied via a
     * constructor. Designed to be used by subclasses.
     * @param className name of class to load
     * @return a <code>Class</code> instance, if the class could be loaded,
     * otherwise null.
     */
    protected Class<?> loadCustomClass(String className) {
        try {
            Class<?> foundClass = super.findClass(className);
            if (logger.isInfoEnabled()) {
                logger.info("Found class " + className + " using class loader " + this);
            }
            loadedClasses.put(className, foundClass);
            return foundClass;
        }
        catch (ClassNotFoundException e1) {
            return null;
        }
    }

    /**
     * Returns a cached <code>Class</code> instance, if it has already been
     * loaded using the <code>ClassLoader</code>. Designed to be used by
     * subclasses.
     * @param className the name of the class to return
     * @return returns the cached <code>Class</code> instance, if previously
     * loaded using this <code>ClassLoader</code>, otherwise null.
     */
    protected Class<?> getAlreadyLoadedClass(String className) {
        Class<?> loadedClass = loadedClasses.get(className);
        if (loadedClass != null) {
            if (logger.isDebugEnabled()) {
                debug("Returning already loaded custom class: " + className);
            }
            return loadedClass;
        }
        return loadedClass;
    }

    /**
     * Attempts to load the designated class by delegating to the parent class
     * loader.
     * @param className the name of the class to load
     * @return a <code>Class</code> instance successfully loaded, otherwise
     * null
     */
    protected Class<?> loadParentClass(String className) {
        try {
            Class<?> parentClass = getParent().loadClass(className);

            if (logger.isDebugEnabled()) {
                debug("Returning from parent class loader: " + getParent() + ": " + parentClass);
            }

            return parentClass;
        }
        catch (Exception e) {
        }
        return null;
    }

    /**
     * Attempt to load a resoure, first by calling
     * <code>getCustomResource</code>. If the resource is not found
     * <code>super.getResource(name)</code> is called.
     */
    @Override
    public URL getResource(String name) {

        final URL url = getCustomResource(name);
        if (url != null) {
            return url;
        }

        return super.getResource(name);
    }

    /**
     * Attempts to find a resource from one of the file system locations
     * specified in a constructor.
     * @param name the name of the resource to load
     * @return a <code>URL</code> instance, if the resource can be found,
     * otherwise null.
     */
    protected URL getCustomResource(String name) {
        return super.findResource(name);
    }

    /**
     * Returns an immutable map of name to <code>Class</code> instances loaded
     * via this class loader
     * @return
     */
    public Map<String, Class<?>> getLoadedClasses() {
        return Collections.unmodifiableMap(loadedClasses);
    }

    private void debug(String message) {
        logger.debug(this.getClass().getSimpleName() + "[" + System.identityHashCode(this) + "]: " + message);
    }

    @Override
    public String toString() {
        return super.toString() + ", URLS: " + Arrays.toString(urls) + ", parent: " + (getParent() != null ? getParent().toString() : " ");
    }

}
