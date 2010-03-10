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
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.Transition;
import org.impalaframework.module.spi.TransitionSet;

/**
 * Implements strategy for determining the module operations required based on comparison of an incoming (new)
 * {@link RootModuleDefinition} and and outgoing (old) {@link RootModuleDefinition}. Traverses
 * and compares the module hierarchy of each.
 * @author Phil Zoio
 */
public class StrictModificationExtractor implements ModificationExtractor {

    public final TransitionSet getTransitions(Application application, RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition) {
        
        if (originalDefinition == null && newDefinition == null) {
            throw new IllegalArgumentException("Either originalDefinition or newDefinition must be non-null");
        }

        originalDefinition = ModuleDefinitionUtils.cloneAndUnfreeze(originalDefinition);
        newDefinition = ModuleDefinitionUtils.cloneAndUnfreeze(newDefinition);
        
        return doGetTransitions(originalDefinition, newDefinition);
    }

    protected TransitionSet doGetTransitions(
            RootModuleDefinition originalDefinition,
            RootModuleDefinition newDefinition) {
        List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
        populateTransitions(transitions, originalDefinition, newDefinition);
        
        if (newDefinition != null) {
            ModuleDefinitionUtils.freeze(newDefinition);
        }
        return new TransitionSet(transitions, newDefinition);
    }

    protected void populateTransitions(List<ModuleStateChange> transitions,
            RootModuleDefinition originalDefinition,
            RootModuleDefinition newDefinition) {
        
        //if new definition is null and old is not, then unload everything
        if (originalDefinition != null && newDefinition == null) {
            unloadDefinitions(originalDefinition, transitions);
        }
        
        //if old definition is null and new is not, then load everything
        else if (newDefinition != null && originalDefinition == null) {
            loadDefinitions(newDefinition, transitions);
        }
        else {
            //check for modifications
            compareRootDefinitions(originalDefinition, newDefinition, transitions);
        }
    }

    protected void compareRootDefinitions(RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
        compare(originalDefinition, newDefinition, transitions);
    }
    
    protected void compare(ModuleDefinition oldDefinition, ModuleDefinition newDefinition, List<ModuleStateChange> transitions) {

        boolean notEqual = !oldDefinition.equals(newDefinition);
        
        // original are not different: reload everything
        if (notEqual) {
            unloadDefinitions(oldDefinition, transitions);
            loadDefinitions(newDefinition, transitions);
        }
        else {
            checkNewAndOriginal(oldDefinition, newDefinition, transitions);
        }
        
        //if marked as stale, reload everything
        if (ModuleState.STALE.equals(newDefinition.getState())) {
            unloadDefinitions(oldDefinition, transitions);
            loadDefinitions(newDefinition, transitions);
        }
    }
    
    protected void checkNewAndOriginal(
            ModuleDefinition originalDefinition,
            ModuleDefinition newDefinition,
            List<ModuleStateChange> transitions) {
        Collection<ModuleDefinition> newChildren = newDefinition.getChildModuleDefinitions();
            //getNewChildDefinitions(newDefinition);
        Collection<ModuleDefinition> oldChildren = originalDefinition.getChildModuleDefinitions();
            //getOldChildDefinitions(originalDefinition);
        
        checkNew(originalDefinition, newDefinition, oldChildren, newChildren, transitions);
        checkOriginal(originalDefinition, newDefinition, oldChildren, newChildren, transitions);
    }

    protected void checkNew(
            ModuleDefinition oldParent, 
            ModuleDefinition newParent, 
            Collection<ModuleDefinition> oldChildren, 
            Collection<ModuleDefinition> newChildren, 
            List<ModuleStateChange> transitions) {
        
        for (ModuleDefinition newChild : newChildren) {
            ModuleDefinition oldChild = ModuleDefinitionUtils.getModuleFromCollection(oldChildren, newChild.getName());

            //if new module has definition not present in old, then load this with children
            if (oldChild == null) {
                loadDefinitions(newChild, transitions);             
            }
            //otherwise, compare
            else {
                compare(oldChild, newChild, transitions);
            }
        }
    }
    
    protected void checkOriginal(
            ModuleDefinition oldParent, 
            ModuleDefinition newParent, 
            Collection<ModuleDefinition> oldChildren, 
            Collection<ModuleDefinition> newChildren, 
            List<ModuleStateChange> transitions) {
    
        for (ModuleDefinition oldChild : oldChildren) {
            ModuleDefinition newChild = ModuleDefinitionUtils.getModuleFromCollection(newChildren, oldChild.getName());

            //if old module has definition which is no longer present, then unload this
            if (newChild == null) {
                unloadDefinitions(oldChild, transitions);
            }
        }
    }

    protected void unloadDefinitions(ModuleDefinition definitionToUnload, List<ModuleStateChange> transitions) {
        
        Collection<ModuleDefinition> childDefinitions = getOldChildDefinitions(definitionToUnload);
        for (ModuleDefinition childDefinition : childDefinitions) {
            unloadDefinitions(childDefinition, transitions);
        }
        ModuleStateChange transition = new ModuleStateChange(Transition.LOADED_TO_UNLOADED, definitionToUnload);
        transitions.add(transition);
        definitionToUnload.setState(ModuleState.UNLOADING);
    }

    protected void loadDefinitions(ModuleDefinition definitionToLoad, List<ModuleStateChange> transitions) {
        ModuleStateChange transition = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, definitionToLoad);
        transitions.add(transition);
        definitionToLoad.setState(ModuleState.LOADING);

        Collection<ModuleDefinition> childDefinitions = getNewChildDefinitions(definitionToLoad);
        for (ModuleDefinition childDefinition : childDefinitions) {
            loadDefinitions(childDefinition, transitions);
        }
    }

    protected Collection<ModuleDefinition> getOldChildDefinitions(ModuleDefinition definition) {
        return getChildDefinitions(definition);
    }
    
    protected Collection<ModuleDefinition> getNewChildDefinitions(ModuleDefinition definition) {
        return getChildDefinitions(definition);
    }

    private Collection<ModuleDefinition> getChildDefinitions(
            ModuleDefinition definitions) {
        Collection<ModuleDefinition> childDefinitions = definitions.getChildModuleDefinitions();
        return childDefinitions;
    }
}
