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

package org.impalaframework.module.operation;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModificationExtractorType;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TransitionResultSet;
import org.impalaframework.module.spi.TransitionSet;
import org.springframework.util.Assert;

/**
 * Encapsulates operation to add a new module defintion, and reflect this new configuration on the module runtime.
 * 
 * @author Phil Zoio
 */
public class AddModuleOperation extends BaseModuleOperation {

    protected AddModuleOperation() {
        super();
    }

    public ModuleOperationResult doExecute(Application application, ModuleOperationInput moduleOperationInput) {
        
        Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
        ModuleDefinition moduleToAdd = moduleOperationInput.getModuleDefinition();
        Assert.notNull(moduleToAdd, "moduleName is required as it specifies the name of the module to add in " + this.getClass().getName());
        
        ModificationExtractor calculator = getModificationExtractorRegistry().getModificationExtractor(ModificationExtractorType.STICKY);
        
        TransitionResultSet transitionResultSet = addModule(application, calculator, moduleToAdd);
        return new ModuleOperationResult(transitionResultSet);
    }
    
    protected TransitionResultSet addModule(
            Application application, 
            ModificationExtractor calculator,
            ModuleDefinition moduleDefinition) {

        ModuleStateHolder moduleStateHolder = application.getModuleStateHolder();
        
        RootModuleDefinition oldRootDefinition = moduleStateHolder.cloneRootModuleDefinition();
        RootModuleDefinition newRootDefinition = moduleStateHolder.cloneRootModuleDefinition();

        ModuleDefinition parent = moduleDefinition.getParentDefinition();
        
        if (moduleDefinition instanceof RootModuleDefinition) {
            newRootDefinition = (RootModuleDefinition) moduleDefinition;
        }
        else {

            ModuleDefinition newParent = null;

            if (parent == null) {
                newParent = newRootDefinition;
            }
            else {
                String parentName = parent.getName();
                newParent = newRootDefinition.findChildDefinition(parentName, true);

                if (newParent == null) {
                    throw new InvalidStateException("Unable to find parent module '" + parentName + "' in " + newRootDefinition);
                }
            }

            newParent.addChildModuleDefinition(moduleDefinition);
            
            moduleDefinition.setParentDefinition(newParent);
        }

        TransitionSet transitions = calculator.getTransitions(application, oldRootDefinition, newRootDefinition);
        return getTransitionManager().processTransitions(moduleStateHolder, application, transitions);
    }   
    
}
