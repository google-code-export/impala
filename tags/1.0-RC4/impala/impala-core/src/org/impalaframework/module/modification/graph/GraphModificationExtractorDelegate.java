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

package org.impalaframework.module.modification.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.DependencyManager;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.modification.StrictModificationExtractor;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.Transition;
import org.impalaframework.module.spi.TransitionSet;

public class GraphModificationExtractorDelegate extends StrictModificationExtractor implements GraphAwareModificationExtractor {
    
    private DependencyManager oldDependencyManager;
    private DependencyManager newDependencyManager;
    
    @Override
    public TransitionSet doGetTransitions(
            RootModuleDefinition originalDefinition,
            RootModuleDefinition newDefinition) {

        List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
        
        populateTransitions(transitions, originalDefinition, newDefinition);
    
        //sort so that they load and unload in the right order
        transitions = sortTransitions(transitions, originalDefinition, newDefinition);
        
        if (newDefinition != null) {
            ModuleDefinitionUtils.freeze(newDefinition);
        }
        return new TransitionSet(transitions, newDefinition);
    }

    List<ModuleStateChange> sortTransitions(List<ModuleStateChange> transitions,
            RootModuleDefinition originalDefinition,
            RootModuleDefinition newDefinition) {
        
        Collection<ModuleDefinition> unloadable = populateAndSortUnloadable(transitions);
        
        Collection<ModuleDefinition> loadable = populateAndSortLoadable(transitions);
        
        //newTransitions
        List<ModuleStateChange> newTransitions = new ArrayList<ModuleStateChange>();
        
        for (ModuleDefinition moduleDefinition : unloadable) {
            newTransitions.add(new ModuleStateChange(Transition.LOADED_TO_UNLOADED, moduleDefinition));
        }
        
        //build loadable
        for (ModuleDefinition moduleDefinition : loadable) {
            newTransitions.add(new ModuleStateChange(Transition.UNLOADED_TO_LOADED, moduleDefinition));
        }
        
        //now add other transitions
        for (ModuleStateChange moduleStateChange : transitions) {
            if (!newTransitions.contains(moduleStateChange)) {
                newTransitions.add(moduleStateChange);
            }
        }
        
        return newTransitions;
    }
    
    protected void populateTransitions(List<ModuleStateChange> transitions,
            RootModuleDefinition originalDefinition,
            RootModuleDefinition newDefinition) {
        if (originalDefinition == null && newDefinition == null) {
            throw new IllegalArgumentException("Either originalDefinition or newDefinition must be non-null");
        }
        
        if (originalDefinition != null) {
            oldDependencyManager = new DependencyManager(originalDefinition);
        }
        
        if (newDefinition != null) {
            newDependencyManager = new DependencyManager(newDefinition);
        }
        
        //get the transitions from the superclass hierarchy
        super.populateTransitions(transitions, originalDefinition, newDefinition);

        //new module definition. Iterate through and populate all siblings
        if (originalDefinition == null && newDefinition != null) {
            final List<ModuleDefinition> newSiblings = newDefinition.getSiblings();
            for (ModuleDefinition moduleDefinition : newSiblings) {
                loadDefinitions(moduleDefinition, transitions);
            }
        }
        //new module definition. Iterate through and unload all siblings
        else if (newDefinition == null && originalDefinition != null) {
            final List<ModuleDefinition> oldSiblings = originalDefinition.getSiblings();
            for (ModuleDefinition moduleDefinition : oldSiblings) {
                unloadDefinitions(moduleDefinition, transitions);
            }
        }
        //Both not null, so we need to update
        else {
            
            final List<ModuleDefinition> oldSiblings = originalDefinition.getSiblings();
            final List<ModuleDefinition> newSiblings = newDefinition.getSiblings();

            //Understanding is that the order is not important

            //unload any siblings in old but not in new
            for (ModuleDefinition oldSibling : oldSiblings) {
                if (!newDefinition.hasSibling(oldSibling.getName())) {
                    unloadDefinitions(oldSibling, transitions);
                }
            }   
            
            //load any siblings in new but not in old
            for (ModuleDefinition newSibling : newSiblings) {
                if (!originalDefinition.hasSibling(newSibling.getName())) {
                    loadDefinitions(newSibling, transitions);
                }
            }
            
            for (ModuleDefinition newSibling : newSiblings) {
                if (originalDefinition.hasSibling(newSibling.getName())) {
                    final ModuleDefinition siblingModule = originalDefinition.getSiblingModule(newSibling.getName());
                    compare(siblingModule, newSibling, transitions);
                }
            }
        }
    }

    @Override
    protected Collection<ModuleDefinition> getNewChildDefinitions(ModuleDefinition definition) {
        return newDependencyManager.getDirectDependants(definition.getName());
    }

    @Override
    protected Collection<ModuleDefinition> getOldChildDefinitions(ModuleDefinition definition) {
        return oldDependencyManager.getDirectDependants(definition.getName());
    }   

    private Collection<ModuleDefinition> populateAndSortLoadable(List<ModuleStateChange> transitions) {
        
        Collection<ModuleDefinition> loadable = new LinkedHashSet<ModuleDefinition>();
        
        //collect unloaded first
        for (ModuleStateChange moduleStateChange : transitions) {
            if (moduleStateChange.getTransition().equals(Transition.UNLOADED_TO_LOADED)) {
                final ModuleDefinition moduleDefinition = moduleStateChange.getModuleDefinition();
                
                //are we likely to get duplicates
                loadable.add(moduleDefinition);
            }
        }
        
        if (newDependencyManager != null) {
            //use dependency manager to sort
            loadable = newDependencyManager.sort(loadable);
        }
        
        return loadable;
    }

    private Collection<ModuleDefinition> populateAndSortUnloadable(List<ModuleStateChange> transitions) {
        
        Collection<ModuleDefinition> unloadable = new LinkedHashSet<ModuleDefinition>();
        
        //collect unloaded first
        for (ModuleStateChange moduleStateChange : transitions) {
            if (moduleStateChange.getTransition().equals(Transition.LOADED_TO_UNLOADED)) {
                final ModuleDefinition moduleDefinition = moduleStateChange.getModuleDefinition();
                
                //are we likely to get duplicates
                unloadable.add(moduleDefinition);
            }
        }
        
        if (oldDependencyManager != null) {
            //use dependency manager to sort in reverse
            unloadable = oldDependencyManager.reverseSort(unloadable);
        }
        return unloadable;
    }

    public DependencyManager getOldDependencyManager() {
        return oldDependencyManager;
    }

    public DependencyManager getNewDependencyManager() {
        return newDependencyManager;
    }

}
