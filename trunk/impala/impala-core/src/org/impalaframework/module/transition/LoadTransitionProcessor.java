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

package org.impalaframework.module.transition;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleRuntimeManager;
import org.impalaframework.module.spi.TransitionProcessor;

public class LoadTransitionProcessor implements TransitionProcessor {

    private static final Log logger = LogFactory.getLog(LoadTransitionProcessor.class);

    private ModuleRuntimeManager moduleRuntimeManager;

    public LoadTransitionProcessor() {
        super();
    }

    public void process(Application application, RootModuleDefinition rootDefinition, ModuleDefinition currentDefinition) {

        final String definitionName = currentDefinition.getName();
        logger.info("Loading definition " + definitionName);
        
        if (ModuleState.DEPENDENCY_FAILED.equals(currentDefinition.getState()))
        {
            logger.info("Not loading module '" + definitionName + "' as one or more of its dependencies failed to load.");
            return;
        }

        boolean success = false;
        Throwable throwable = null;
        
        try {
            success = moduleRuntimeManager.initModule(application, currentDefinition);
        } catch (Throwable e) {
            throwable = e;
            currentDefinition.setState(ModuleState.ERROR);
            logger.error("Failed to load module " + definitionName, e);
        }
        
        if (!success) {

            currentDefinition.setState(ModuleState.ERROR);
            
            final Collection<ModuleDefinition> dependents = ModuleDefinitionUtils.getDependentModules(rootDefinition, currentDefinition.getName());
            
                for (ModuleDefinition moduleDefinition : dependents) {              if (logger.isDebugEnabled()) {
                    logger.debug("Marking '" + moduleDefinition.getName() + "' to state " + ModuleState.DEPENDENCY_FAILED);
                }
                moduleDefinition.setState(ModuleState.DEPENDENCY_FAILED);
            }
        } else {
            currentDefinition.setState(ModuleState.LOADED);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Marked '" + currentDefinition.getName() + "' to state " + currentDefinition.getState());
        }
        
        if (throwable != null) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException)throwable;
            } else {
                throw new org.impalaframework.exception.RuntimeException(throwable);
            }
        }
    }

    public void setModuleRuntimeManager(ModuleRuntimeManager moduleRuntimeManager) {
        this.moduleRuntimeManager = moduleRuntimeManager;
    }

}
