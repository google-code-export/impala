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

package org.impalaframework.module.holder;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.module.spi.ClassLoaderRegistry;
import org.springframework.util.Assert;

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

    public void setApplicationClassLoader(ClassLoader classLoader) {
        this.applicationClassLoader = classLoader;
    }

    public ClassLoader getClassLoader(String moduleName) {
        checkModuleName(moduleName);
        return classLoaders.get(moduleName);
    }

    private void checkModuleName(String moduleName) {
        Assert.notNull(moduleName, "moduleName cannot be null");
    }
    
    public void addClassLoader(String moduleName, ClassLoader classLoader) {
        checkModuleName(moduleName);
        Assert.notNull(classLoader, "classLoader cannot be null");
        synchronized (classLoaders) {
            classLoaders.put(moduleName, classLoader);
        }
    }
    
    public ClassLoader removeClassLoader(String moduleName) {
        checkModuleName(moduleName);
        synchronized (classLoaders) {
            return classLoaders.remove(moduleName);
        }
    }

    public boolean hasClassLoaderFor(String moduleName) {
        checkModuleName(moduleName);
        return classLoaders.containsKey(moduleName);
    }

}
