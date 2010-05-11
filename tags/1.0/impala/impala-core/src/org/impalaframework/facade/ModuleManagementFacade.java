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

package org.impalaframework.facade;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.module.spi.ModuleRuntimeManager;
import org.impalaframework.module.spi.ModuleStateChangeListener;
import org.impalaframework.module.spi.ModuleStateChangeNotifier;
import org.impalaframework.module.spi.TransitionManager;
import org.impalaframework.module.spi.TransitionProcessor;
import org.impalaframework.module.spi.TypeReader;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.BeanFactory;

/**
 * A facade by which many of the Impala module management interfaces can be
 * accessed.
 * 
 * @author Phil Zoio
 */
public interface ModuleManagementFacade extends BeanFactory {

    /**
     * Returns the {@link ModuleLocationResolver} used to resolve classes and resources for modules
     */
    ModuleLocationResolver getModuleLocationResolver();

    /**
     * Returns the {@link ModuleLoaderRegistry} used to hold {@link ModuleLoader} instances for the different types of modules 
     */
    ModuleLoaderRegistry getModuleLoaderRegistry();

    /**
     * Returns the registry used to hold {@link ModificationExtractor} used to identify changes in {@link ModuleDefinition} across
     * module operations, and determine the runtime operations required
     */
    ModificationExtractorRegistry getModificationExtractorRegistry();

    /**
     * Returns the registry used to hold {@link TransitionProcessor} instances used to process changes identified in 
     * {@link ModuleDefinition}s via the {@link ModificationExtractor}s
     */
    TransitionProcessorRegistry getTransitionProcessorRegistry();
    
    /**
     * Returns the {@link ApplicationManager} manager used to access {@link Application} instances 
     */
    ApplicationManager getApplicationManager();

    /**
     * Holds registry of predefined {@link ModuleOperation} instances
     */
    ModuleOperationRegistry getModuleOperationRegistry();

    /**
     * Notifies registered {@link ModuleStateChangeListener} when changes have been made to modules
     */
    ModuleStateChangeNotifier getModuleStateChangeNotifier();

    /**
     * Returns the registry of module type specific {@link TypeReader} instances
     */
    TypeReaderRegistry getTypeReaderRegistry();
    
    /**
     * Returns the {@link ModuleRuntimeManager} used for high level module life cycle management operations
     */
    ModuleRuntimeManager getModuleRuntimeManager();
    
    /**
     * Returns the {@link FrameworkLockHolder} used for managing locking of framework to allow
     * dynamic updates to have necessary concurrency protection
     */
    FrameworkLockHolder getFrameworkLockHolder();
    
    /**
     * Returns the instance of {@link TransitionManager} used to process transitions to reflect the 
     * metadata changes necessary for the current module change operation
     */
    TransitionManager getTransitionManager();

    /**
     * Used to shut down Impala runtime
     */
    void close();

}
