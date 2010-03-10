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

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModificationExtractorType;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TransitionResultSet;
import org.impalaframework.module.spi.TransitionSet;
import org.springframework.util.Assert;

/**
 * {@link ModuleOperation} implementation which contains functionality for implementing reloading of a named module.
 * Unlike {@link ReloadModuleNamedLikeOperation}, this class requires an exact match of the module to be renamed.
 * It searches the module hierarchy/graph, and reloads the first module whose name equals the supplied
 * String.
 * 
 * @see ReloadNamedModuleLikeOperation
 * @author Phil Zoio
 */
public class ReloadNamedModuleOperation  extends BaseModuleOperation {

    protected ReloadNamedModuleOperation() {
        super();
    }

    public ModuleOperationResult doExecute(Application application, ModuleOperationInput moduleOperationInput) {

        Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
        String moduleToReload = moduleOperationInput.getModuleName();
        Assert.notNull(moduleToReload, "moduleName is required as it specifies the name of the module to reload in "
                + this.getClass().getName());

        ModuleStateHolder moduleStateHolder = application.getModuleStateHolder();
        RootModuleDefinition oldRootDefinition = moduleStateHolder.cloneRootModuleDefinition();
        RootModuleDefinition newRootDefinition = moduleStateHolder.cloneRootModuleDefinition();

        ModificationExtractorRegistry modificationExtractor = getModificationExtractorRegistry();
        ModificationExtractor calculator = modificationExtractor
                .getModificationExtractor(ModificationExtractorType.STRICT);

        ModuleDefinition childDefinition = newRootDefinition.findChildDefinition(moduleToReload, true);

        if (childDefinition != null) {
            childDefinition.setState(ModuleState.STALE);

            TransitionSet transitions = calculator.getTransitions(application, oldRootDefinition, newRootDefinition);
            TransitionResultSet transitionResultSet = getTransitionManager().processTransitions(moduleStateHolder, application, transitions);

            boolean result = !transitions.getModuleTransitions().isEmpty();
            return result ? new ModuleOperationResult(transitionResultSet) : ModuleOperationResult.EMPTY;
        }
        
        return ModuleOperationResult.EMPTY;
    }
}
