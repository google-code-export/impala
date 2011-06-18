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

package org.impalaframework.module.modification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.DependencyManager;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.Transition;
import org.impalaframework.module.spi.TransitionSet;

/**
 * Builds transition set which corresponds with moving all modules currently in {@link ModuleState#ERROR}
 * or {@link ModuleState#DEPENDENCY_FAILED} into {@link ModuleState#LOADED}.
 * 
 * @author Phil Zoio
 */
public class RepairModificationExtractor implements ModificationExtractor {

    public TransitionSet getTransitions(Application application, RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition) {

        List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
        
        RootModuleDefinition copy = ModuleDefinitionUtils.cloneAndUnfreeze(originalDefinition);
        DependencyManager dependencyManager = new DependencyManager(copy);
        Collection<ModuleDefinition> sortedModules = dependencyManager.getAllModules();
        
        for (ModuleDefinition moduleDefinition : sortedModules) {
            if (ModuleState.ERROR.equals(moduleDefinition.getState()) || ModuleState.DEPENDENCY_FAILED.equals(moduleDefinition.getState())) {
                transitions.add(new ModuleStateChange(Transition.UNLOADED_TO_LOADED, moduleDefinition));
                moduleDefinition.setState(ModuleState.LOADING);
            }
        }
        
        copy.freeze();
        return new TransitionSet(transitions, copy);
    }

}
