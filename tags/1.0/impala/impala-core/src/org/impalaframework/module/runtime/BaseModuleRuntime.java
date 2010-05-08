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

package org.impalaframework.module.runtime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ClassLoaderRegistry;
import org.impalaframework.module.spi.ModuleRuntime;
import org.impalaframework.module.spi.ModuleRuntimeMonitor;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * Base implementation of {@link ModuleRuntime} interface. Does not contain
 * support for loading or unloading of modules of a particular runtime type
 * (e.g. Spring). However, does implements some generic {@link ModuleRuntime}
 * methods.
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleRuntime implements ModuleRuntime {

    private static Log logger = LogFactory.getLog(BaseModuleRuntime.class);
    
    private ModuleRuntimeMonitor moduleRuntimeMonitor;
    
    private ClassLoaderFactory classLoaderFactory;
    
    /* ********************* ModuleRuntime method implementation ********************* */

    public final RuntimeModule loadRuntimeModule(Application application, ModuleDefinition definition) {
        
        final ClassLoaderRegistry classLoaderRegistry = application.getClassLoaderRegistry();
        
        try {
            beforeModuleLoads(definition);
            
            RuntimeModule runtimeModule;
            try {
                runtimeModule = doLoadModule(application, definition);
            }
            catch (RuntimeException e) {
                classLoaderRegistry.removeClassLoader(definition.getName());
                throw e;
            }
            
            Assert.notNull(classLoaderRegistry);
            
            final String moduleName = definition.getName();
            //note that GraphClassLoaderFactory will also populate the ClassLoaderRegistry, hence, this check
            if (!classLoaderRegistry.hasClassLoaderFor(moduleName)) {
                classLoaderRegistry.addClassLoader(moduleName, runtimeModule.getClassLoader());
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Added new class loader " + ObjectUtils.identityToString(runtimeModule.getClassLoader()) 
                            + " to class loader registry for module: " + moduleName);
                }
            }
            
            return runtimeModule;
        } finally {
            afterModuleLoaded(definition);
        }
    }
    
    public final void closeModule(Application application, RuntimeModule runtimeModule) {

        final ClassLoaderRegistry classLoaderRegistry = application.getClassLoaderRegistry();
        
        final ModuleDefinition moduleDefinition = runtimeModule.getModuleDefinition();
        classLoaderRegistry.removeClassLoader(moduleDefinition.getName());
        doCloseModule(application.getId(), runtimeModule);
    }
    
    /* ********************* Abstract methods ********************* */

    protected abstract void doCloseModule(String applicationId, RuntimeModule runtimeModule);

    /* ********************* Protected methods ********************* */

    /**
     * Lets a {@link ModuleRuntimeMonitor} know that module loading is about to start, if one is wired in.
     */
    protected void beforeModuleLoads(ModuleDefinition definition) {
        if (moduleRuntimeMonitor != null) {
            moduleRuntimeMonitor.beforeModuleLoads(definition);
        }
    }

    /**
     * Lets a {@link ModuleRuntimeMonitor} know that module loading is complete, if one is wired in.
     */
    protected void afterModuleLoaded(ModuleDefinition definition) {
        if (moduleRuntimeMonitor != null) {
            moduleRuntimeMonitor.afterModuleLoaded(definition);
        }
    }
    
    protected RuntimeModule doLoadModule(Application application, ModuleDefinition definition) {
        final ClassLoaderRegistry classLoaderRegistry = application.getClassLoaderRegistry();
        
        ClassLoader parentClassLoader = null;
        final ModuleDefinition parentDefinition = definition.getParentDefinition();

        if (parentDefinition != null) {
            parentClassLoader = classLoaderRegistry.getClassLoader(parentDefinition.getName());
        }
        
        if (parentClassLoader == null) {
            parentClassLoader = ClassUtils.getDefaultClassLoader();
        }
        
        final ClassLoader classLoader = classLoaderFactory.newClassLoader(application, parentClassLoader, definition);
        return doLoadModule(application, classLoader, definition);
    }
    
    /* ********************* wired in setters ********************* */

    protected abstract RuntimeModule doLoadModule(Application application, ClassLoader classLoader, ModuleDefinition parentDefinition);

    public void setModuleRuntimeMonitor(ModuleRuntimeMonitor moduleRuntimeMonitor) {
        this.moduleRuntimeMonitor = moduleRuntimeMonitor;
    }
    
    public void setClassLoaderFactory(ClassLoaderFactory classLoaderFactory) {
        this.classLoaderFactory = classLoaderFactory;
    }
}
