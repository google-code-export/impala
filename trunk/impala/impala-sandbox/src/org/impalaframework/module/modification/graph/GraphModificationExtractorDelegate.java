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

package org.impalaframework.module.modification.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.impalaframework.classloader.graph.DependencyManager;
import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.Transition;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.graph.GraphRootModuleDefinition;
import org.impalaframework.module.modification.StrictModificationExtractor;
import org.springframework.util.Assert;

//FIXME add StickyGraphModificationExtractor
public class GraphModificationExtractorDelegate extends StrictModificationExtractor {
	
	private DependencyManager oldDependencyManager;
	private DependencyManager newDependencyManager;
	
	@Override
	public TransitionSet getTransitions(
			RootModuleDefinition originalDefinition,
			RootModuleDefinition newDefinition) {

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
		
		populateTransitions(transitions, originalDefinition, newDefinition);
		
		//TODO still todo is to order the transitions so that they load and unload in the right order
		
		//sort transitions
		transitions = sortTransitions(transitions, originalDefinition, newDefinition);
		
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
		
		//FIXME add other types of operations
		return newTransitions;
	}
	
	protected void populateTransitions(List<ModuleStateChange> transitions,
			RootModuleDefinition originalDefinition,
			RootModuleDefinition newDefinition) {
		if (originalDefinition == null && newDefinition == null) {
			throw new IllegalArgumentException("Either originalDefinition or newDefinition must be non-null");
		}
		
		if (originalDefinition != null) {
			//FIXME remove this condition when GraphRootModuleDefinition is merged into RootModuleDefinition
			Assert.isTrue(originalDefinition instanceof GraphRootModuleDefinition, 
					originalDefinition + " is not an instance of " + GraphRootModuleDefinition.class);
			
			oldDependencyManager = new DependencyManager((GraphRootModuleDefinition) originalDefinition);
		}
		
		if (newDefinition != null) {
			//FIXME remove this condition when GraphRootModuleDefinition is merged into RootModuleDefinition
			Assert.isTrue(newDefinition instanceof GraphRootModuleDefinition, 
					newDefinition + " is not an instance of " + GraphRootModuleDefinition.class);

			newDependencyManager = new DependencyManager((GraphRootModuleDefinition) newDefinition);
		}
		
		//get the transitions from the superclass hierarchy
		super.populateTransitions(transitions, originalDefinition, newDefinition);

		//new module definition. Iterate through and populate all siblings
		if (originalDefinition == null && newDefinition != null) {
			GraphRootModuleDefinition graph = (GraphRootModuleDefinition) newDefinition;
			final ModuleDefinition[] newSiblings = graph.getSiblings();
			for (ModuleDefinition moduleDefinition : newSiblings) {
				loadDefinitions(moduleDefinition, transitions);
			}
		}
		//new module definition. Iterate through and unload all siblings
		else if (newDefinition == null && originalDefinition != null) {
			GraphRootModuleDefinition graph = (GraphRootModuleDefinition) originalDefinition;
			final ModuleDefinition[] oldSiblings = graph.getSiblings();
			for (ModuleDefinition moduleDefinition : oldSiblings) {
				unloadDefinitions(moduleDefinition, transitions);
			}
		}
		//Both not null, so we need to update
		else {
			
			GraphRootModuleDefinition oldGraph = (GraphRootModuleDefinition) originalDefinition;
			final List<ModuleDefinition> oldSiblings = Arrays.asList(oldGraph.getSiblings());
			
			GraphRootModuleDefinition newGraph = (GraphRootModuleDefinition) newDefinition;
			final List<ModuleDefinition> newSiblings = Arrays.asList(newGraph.getSiblings());

			//Understanding is that the order is not important

			//unload any siblings in old but not in new
			for (ModuleDefinition oldSibling : oldSiblings) {
				if (!newGraph.hasSibling(oldSibling.getName())) {
					unloadDefinitions(oldSibling, transitions);
				}
			}	
			
			//load any siblings in new but not in old
			for (ModuleDefinition newSibling : newSiblings) {
				if (!oldGraph.hasSibling(newSibling.getName())) {
					loadDefinitions(newSibling, transitions);
				}
			}
			
			for (ModuleDefinition newSibling : newSiblings) {
				if (oldGraph.hasSibling(newSibling.getName())) {
					final ModuleDefinition siblingModule = oldGraph.getSiblingModule(newSibling.getName());
					compare(siblingModule, newSibling, transitions);
				}
			}
		}
	}

	@Override
	protected Collection<ModuleDefinition> getNewChildDefinitions(ModuleDefinition definition) {
		return newDependencyManager.getDirectDependees(definition.getName());
	}

	@Override
	protected Collection<ModuleDefinition> getOldChildDefinitions(ModuleDefinition definition) {
		return oldDependencyManager.getDirectDependees(definition.getName());
	}	

	private Collection<ModuleDefinition> populateAndSortLoadable(
			List<ModuleStateChange> transitions) {
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

	private Collection<ModuleDefinition> populateAndSortUnloadable(
			List<ModuleStateChange> transitions) {
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
