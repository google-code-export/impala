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
 * Implementation of {@link ModuleOperation} which encapsulates the mechanism to remove a named module.
 * 
 * @author Phil Zoio
 */
public class RemoveModuleOperation  extends BaseModuleOperation {

    protected RemoveModuleOperation() {
        super();
    }

    public ModuleOperationResult doExecute(Application application, ModuleOperationInput moduleOperationInput) {
        
        Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
        String moduleToRemove = moduleOperationInput.getModuleName();
        Assert.notNull(moduleToRemove, "moduleName is required as it specifies the name of the module to remove in " + this.getClass().getName());
        
        ModuleStateHolder moduleStateHolder = application.getModuleStateHolder();
        ModificationExtractor calculator = getModificationExtractorRegistry().getModificationExtractor(ModificationExtractorType.STRICT);
        TransitionResultSet transitionResultSet = removeModule(moduleStateHolder, application, calculator, moduleToRemove);
        
        boolean hasResults = transitionResultSet.hasResults();
        return hasResults ? new ModuleOperationResult(transitionResultSet) : ModuleOperationResult.EMPTY;
    }
    
    protected TransitionResultSet removeModule(ModuleStateHolder moduleStateHolder, Application application,
            ModificationExtractor calculator, String moduleToRemove) {
        
        RootModuleDefinition oldRootDefinition = moduleStateHolder.getRootModuleDefinition();
        
        if (oldRootDefinition == null) {
            return new TransitionResultSet();
        }
        
        RootModuleDefinition newRootDefinition = moduleStateHolder.cloneRootModuleDefinition();
        ModuleDefinition definitionToRemove = newRootDefinition.findChildDefinition(moduleToRemove, true);

        if (definitionToRemove != null) {
            if (definitionToRemove instanceof RootModuleDefinition) {
                //we're removing the rootModuleDefinition
                TransitionSet transitions = calculator.getTransitions(application, oldRootDefinition, null);
                TransitionResultSet transitionResultSet = getTransitionManager().processTransitions(moduleStateHolder, application, transitions);
                return transitionResultSet;
            }
            else {
                ModuleDefinition parent = definitionToRemove.getParentDefinition();
                if (parent != null) {
                    parent.removeChildModuleDefinition(moduleToRemove);
                    
                    definitionToRemove.setParentDefinition(null);

                    TransitionSet transitions = calculator.getTransitions(application, oldRootDefinition, newRootDefinition);
                    TransitionResultSet transitionResultSet = getTransitionManager().processTransitions(moduleStateHolder, application, transitions);
                    return transitionResultSet;
                }
                else {
                    throw new InvalidStateException("Module to remove does not have a parent module. "
                            + "This is unexpected state and may indicate a bug");
                }
            }
        }
        return new TransitionResultSet();
    }
    
}
