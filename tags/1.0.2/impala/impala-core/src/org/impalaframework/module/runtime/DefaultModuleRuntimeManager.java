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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ExecutionException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleRuntime;
import org.impalaframework.module.spi.ModuleRuntimeManager;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.registry.Registry;
import org.impalaframework.registry.RegistrySupport;
import org.impalaframework.service.ServiceRegistry;

/**
 * Implementation of {@link ModuleRuntimeManager}. Responsible for delegating
 * call to create {@link RuntimeModule} to {@link ModuleRuntime}, and making
 * this available to the {@link ModuleStateHolder}.
 * 
 * @author Phil Zoio
 */
public class DefaultModuleRuntimeManager extends RegistrySupport implements ModuleRuntimeManager, Registry<ModuleRuntime> {

    private static final Log logger = LogFactory.getLog(DefaultModuleRuntimeManager.class);
    
    public boolean initModule(Application application, ModuleDefinition currentDefinition) {
        
        boolean success = true;
        
        final String moduleName = currentDefinition.getName();
        logger.info("Loading definition " + moduleName);
        
        final ModuleStateHolder moduleStateHolder = application.getModuleStateHolder();
        final ServiceRegistry serviceRegistry = application.getServiceRegistry();
        
        if (moduleStateHolder.getModule(moduleName) == null) {

            ModuleRuntime moduleRuntime = getModuleRuntime(currentDefinition);
            
            try {
                RuntimeModule runtimeModule = moduleRuntime.loadRuntimeModule(application, currentDefinition);
                moduleStateHolder.putModule(moduleName, runtimeModule);
            }
            catch (Throwable e) {
                
                try {
                    serviceRegistry.evictModuleServices(moduleName);
                } catch (Exception ee) {
                    logger.error("Error evicting modules from module: " + moduleName, ee);
                }
                
                logger.error("Failed to handle loading of application module: " + moduleName, e);
                
                if (e instanceof RuntimeException) {
                    throw (RuntimeException)e;
                } else {
                    throw new ExecutionException(e.getMessage(), e);
                }
            }

        }
        else {
            logger.warn("Attempted to load module " + moduleName
                    + " which was already loaded. Suggest calling unload first.");
            success = false;
        }

        return success;
    }
    
    public boolean closeModule(Application application, ModuleDefinition currentDefinition) {

        final String moduleDefinition = currentDefinition.getName();
        logger.info("Unloading module " + moduleDefinition);

        boolean success = true;

        final ModuleStateHolder moduleStateHolder = application.getModuleStateHolder();
        
        RuntimeModule runtimeModule = moduleStateHolder.removeModule(moduleDefinition);
        if (runtimeModule != null) {
            try {
                ModuleRuntime moduleRuntime = getModuleRuntime(currentDefinition);
                moduleRuntime.closeModule(application, runtimeModule);
            }
            catch (RuntimeException e) {
                
                logger.error("Failed to handle unloading of application module " + moduleDefinition, e);
                if (e instanceof RuntimeException) {
                    throw (RuntimeException)e;
                } else {
                    throw new ExecutionException(e.getMessage(), e);
                }
            }
        }
        return success;
    }

    final ModuleRuntime getModuleRuntime(ModuleDefinition currentDefinition) {
        final String runtimeFramework = currentDefinition.getRuntimeFramework();
        ModuleRuntime moduleRuntime = super.getEntry(runtimeFramework, ModuleRuntime.class);
        return moduleRuntime;
    }

    public void addItem(String name, ModuleRuntime moduleRuntime) {
        super.addRegistryItem(name, moduleRuntime);
    }

    public void setModuleRuntimes(Map<String, ModuleRuntime> moduleRuntimes) {
        super.setEntries(moduleRuntimes);
    }

}
