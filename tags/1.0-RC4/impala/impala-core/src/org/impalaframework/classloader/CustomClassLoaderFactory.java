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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of {@link ClassLoaderFactory} which returns either an instance
 * of {@link ParentClassLoader} or {@link ModuleClassLoader}, depending on
 * whether the {@link #parentClassLoaderFirst} field is set to true or not.
 * 
 * @author Phil Zoio
 */
public class CustomClassLoaderFactory extends BaseModuleClassLoaderFactory {
    
    private static final Log logger = LogFactory.getLog(CustomClassLoaderFactory.class);
    
    private boolean parentClassLoaderFirst;

    public ClassLoader newClassLoader(ClassLoader parent, File[] files) {
        ClassLoader classLoader = null;
        if (parentClassLoaderFirst) { 
            classLoader =  new ParentClassLoader(parent, files);
        }
        else {
            classLoader = new ModuleClassLoader(parent, files);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Returning class loader " + classLoader + " for files " + Arrays.asList(files));
        }
        
        return classLoader;
    }

    public ClassLoader newClassLoader(ClassLoader parent, URL[] urls) {
        ClassLoader classLoader = null;
        if (parentClassLoaderFirst) { 
            classLoader =  new ParentClassLoader(parent, urls);
        }
        else {
            classLoader = new ModuleClassLoader(parent, urls);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Returning class loader " + classLoader + " for urls " + Arrays.asList(urls));
        }
        
        return classLoader;
    }

    public void setParentClassLoaderFirst(boolean loadParentFirst) {
        this.parentClassLoaderFirst = loadParentFirst;
    }

}
