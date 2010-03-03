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
import java.lang.instrument.ClassFileTransformer;
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
            ModuleDefinition definition, 
            boolean loadParentFirst) {
        
        super(parentClassLoader);
        this.moduleDefinition = definition;
        this.classRetriever = classRetriever;
        this.delegateClassLoader = delegateClassLoader;
        this.loadParentFirst = loadParentFirst;     
    }
    
    /**
     * Calls {@link #loadClass(String, boolean)} with resolve set to false
     */
    @Override
    public synchronized Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }
    
    /**
     * Attempts to load class for given name. If {@link #loadParentFirst} is set true, 
     * then will first delegate to the parent class loader, which will typically either be the 
     * system class loader or the web application class loader for web applications.
     * It will then attempt to load the class from one of the modules, starting with the 
     * root module and modules without other dependencies, finally ending searching within
     * the current module (the module with with which this class loader instance is associated).
     * 
     *  Note that if {@link #loadParentFirst} is set to false, then the module graph is searched 
     *  first before delegating to the parent class loader. This is particularly useful in environments
     *  where certain modules are on the system class path (for example, when running integration tests as a 
     *  test suite within Eclipse).
     */
    @Override
    public Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        
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
            if (resolve) {
                resolveClass(loadClass);
            }
            return loadClass;
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Unable to find class " + className);
            logger.debug("Using class loader: " + this);
        }
        
        throw new ClassNotFoundException("Unable to find class " + className);
    }

    /**
     * Implements the mechanism for loading a class within the module.
     * 
     * First checks to see whether the class is present in the local class loader cache.
     * If not, and <code>tryDelegate</code> is true, then will pass the request to the {@link #delegateClassLoader}
     * instance, which will attempts to load the class from dependent modules.
     * 
     * Finally, if the class is not found, then attempts to load it locally within the current module.
     * If the class is still not found, a {@link ClassNotFoundException} is thrown.
     */
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

    /**
     * Returns true if classes loaded by the specified class loader are visible to the current class loader.
     */
    public boolean hasVisibilityOf(ClassLoader classLoader){
        if (classLoader == this) {
            return true;
        }
        boolean hasVisibilityOf = delegateClassLoader.hasVisibilityOf(classLoader);
        
        if (hasVisibilityOf)
            return hasVisibilityOf;
        
        ClassLoader child = this;
        ClassLoader parent = null;

        while ((parent = child.getParent()) != null) {
            if (parent == classLoader) {
                return true;
            }
            child = parent;
        }
        
        return false;
    }

    
    /**
     * Attempt to load a resource, first by calling
     * <code>getLocalResource</code>. If the resource is not found, the
     * {@link #delegateClassLoader} is asked to locate the resource.
     * 
     * If neither the local class loader nor the delegates can find the
     * resource, this means it is not within the module class path. At this
     * point, the rest of the class path is searched via a
     * <code>super.getResource(name)</code> class.
     * 
     * Note that while class loading first delegates the class loading request,
     * resource loading effectively works in reverse. When delegating the
     * resource loading request, modules nearest to the current module are
     * checked first. The check for the resource is only delegated to the system
     * or web application class loader if the resource is not found within the
     * module hierarchy.
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
     * Returns enumeration of local resources, combined with those of parent
     * class loader.
     * 
     * The implementation of {@link #getResources(String)} is pretty
     * conservative. For a given resource name, only one resource URL will ever
     * be found from within the module hierarchy. Also, only the current module
     * is searched. It's ancestor and dependent modules are not searched for the
     * resource.
     * 
     * The module resource, if found, is returned as an {@link Enumeration}
     * which also includes the resources obtained from the standard or
     * application class loader via the super{@link #getResources(String)} call.
     */
    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL> resources = super.getResources(name);
        
        Enumeration<URL> localResources = getLocalResources(name);
        if (localResources != null) {
            List<URL> combined = new ArrayList<URL>();
            combined.addAll(Collections.list(localResources));
            combined.addAll(Collections.list(resources));
            return Collections.enumeration(combined);
        }
        
        return resources;
    }

    /**
     * Attempts to find a resource from one of the file system locations
     * specified in a constructor.
     * 
     * @param name the name of the resource to load
     * @return a <code>URL</code> instance, if the resource can be found,
     * otherwise null.
     */
    protected URL getLocalResource(String name) {
        return classRetriever.findResource(name);
    }
    
    protected Enumeration<URL> getLocalResources(String name) {
        return classRetriever.findResources(name);
    }
    
    Map<String, Class<?>> getLoadedClasses() {
        return Collections.unmodifiableMap(loadedClasses);
    }

    protected final String getModuleName() {
        return moduleDefinition.getName();
    }
    
    public ClassRetriever getClassRetriever() {
        return classRetriever;
    }
    
    public void addTransformer(ClassFileTransformer transformer) {
        logger.warn("No-op implementation of 'addTransformer()' invoked. Use 'load.time.weaving.enabled=true' and start JVM with '-javaagent:/path_to_aspectj_weaver/aspectjweaver.jar' switch to enable load time weaving of aspects.");
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            if (Boolean.valueOf(System.getProperty("display.classloader.finalize"))) {
                System.out.println("Finalizing class loader for " + moduleDefinition.getName());
            }
            if (Boolean.valueOf(System.getProperty("log.classloader.finalize"))) {
                logger.info("Finalizing class loader for " + moduleDefinition.getName());
            }
        }
        finally {
            super.finalize();
        }
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
