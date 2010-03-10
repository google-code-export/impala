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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.springframework.beans.factory.BeanClassLoaderAware;

/**
 * Trivial implementation of {@link ClassLoader}. Exposes
 * {@link BeanClassLoaderAware#setBeanClassLoader(ClassLoader)} to capture the
 * class loader which loads the Impala application context. Simply returns this
 * when {@link #newClassLoader(Application, ClassLoader, ModuleDefinition)} is called.
 * 
 * @author Phil Zoio
 */
public class SharedClassLoaderFactory implements ClassLoaderFactory, BeanClassLoaderAware {

    private static Log logger = LogFactory.getLog(SharedClassLoaderFactory.class);
    
    private ClassLoader classLoader;
    
    /**
     * Returns the wired in {@link ClassLoader} instance.
     */
    public ClassLoader newClassLoader(Application application, ClassLoader parent, ModuleDefinition moduleDefinition) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Returning shared class loader " + classLoader);
        }
        
        return classLoader;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
