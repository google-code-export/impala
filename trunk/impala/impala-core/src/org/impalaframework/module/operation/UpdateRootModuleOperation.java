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

package org.impalaframework.module.operation;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModificationExtractorType;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TransitionResultSet;
import org.impalaframework.module.spi.TransitionSet;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ModuleOperation} which encapsulates mechanism for
 * updating the module hierarchy to reflect the {@link ModuleDefinition}
 * obtained from the {@link ModuleOperationInput#getModuleDefinitionSource()}
 * instance. This operation can be used to supply a new module definition set to
 * reflect a new desired module hierarchy.
 * 
 * @author Phil Zoio
 */
public class UpdateRootModuleOperation  extends BaseModuleOperation {

    protected UpdateRootModuleOperation() {
        super();
    }

    public ModuleOperationResult doExecute(Application application, ModuleOperationInput moduleOperationInput) {

        Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
        ModuleStateHolder moduleStateHolder = application.getModuleStateHolder();
        
        //note that the module definition source is externally supplied
        ModuleDefinitionSource newModuleDefinitionSource = moduleOperationInput.getModuleDefinitionSource();
        Assert.notNull(newModuleDefinitionSource, "moduleDefinitionSource is required as it specifies the new module definition to apply in " + this.getClass().getName());
        
        RootModuleDefinition newModuleDefinition = newModuleDefinitionSource.getModuleDefinition();
        RootModuleDefinition oldModuleDefinition = getExistingModuleDefinitionSource(application);
        
        ModificationExtractorType modificationExtractorType = getModificationExtractorType();
        
        // figure out the modules to reload
        ModificationExtractor calculator = getModificationExtractorRegistry().getModificationExtractor(modificationExtractorType);
        
        //FIXME issue 293 test
        if (oldModuleDefinition == null && newModuleDefinition == null) {
            return ModuleOperationResult.EMPTY;
        }
        
        TransitionSet transitions = calculator.getTransitions(application, oldModuleDefinition, newModuleDefinition);
        TransitionResultSet transitionResultSet = getTransitionManager().processTransitions(moduleStateHolder, application, transitions);
        return new ModuleOperationResult(transitionResultSet);
    }

    protected ModificationExtractorType getModificationExtractorType() {
        return ModificationExtractorType.STRICT;
    }

    protected RootModuleDefinition getExistingModuleDefinitionSource(Application application) {
        return null;
    }
}
