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

package org.impalaframework.module.modification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impalaframework.module.ModificationExtractor;
import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.Transition;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleState;
import org.impalaframework.module.definition.RootModuleDefinition;

/**
 * Implements strategy for determining the module operations required based on comparison of an incoming (new)
 * {@link RootModuleDefinition} and and outgoing (old) {@link RootModuleDefinition}. Traverses
 * and compares the module hierarchy of each.
 * @author Phil Zoio
 */
public class StrictModificationExtractor implements ModificationExtractor {

	@SuppressWarnings("unchecked")
	public TransitionSet getTransitions(RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition) {

		if (originalDefinition == null && newDefinition == null) {
			throw new IllegalArgumentException("Either originalDefinition or newDefinition must be non-null");
		}

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
		populateTransitions(transitions, originalDefinition, newDefinition);
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
			Collection<ModuleDefinition> newDefinitions = newDefinition.getChildDefinitions();
			checkNew(oldDefinition, newDefinitions, transitions);
			checkOriginal(oldDefinition, newDefinition, transitions);
		}
		
		//if marked as stale, reload everything
		if (ModuleState.STALE.equals(newDefinition.getState())) {
			unloadDefinitions(oldDefinition, transitions);
			loadDefinitions(newDefinition, transitions);
		}
	}

	protected void checkNew(ModuleDefinition originalDefinition, Collection<ModuleDefinition> definitions, List<ModuleStateChange> transitions) {
		for (ModuleDefinition definition : definitions) {
			ModuleDefinition oldDefinition = originalDefinition.getModule(definition.getName());

			//if new module has definition not present in old, then load this with children
			if (oldDefinition == null) {
				loadDefinitions(definition, transitions);				
			}
			//otherwise, compare
			else {
				compare(oldDefinition, definition, transitions);
			}
		}
	}

	protected void checkOriginal(ModuleDefinition originalDefinition, ModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> oldDefinitions = originalDefinition.getChildDefinitions();
		
		for (ModuleDefinition oldDefinition : oldDefinitions) {
			ModuleDefinition newDef = newDefinition.getModule(oldDefinition.getName());

			//if old module has definition which is no longer present, then unload this
			if (newDef == null) {
				unloadDefinitions(oldDefinition, transitions);
			}
		}
	}

	protected void unloadDefinitions(ModuleDefinition definitionToUnload, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> childDefinitions = definitionToUnload.getChildDefinitions();
		for (ModuleDefinition childDefinition : childDefinitions) {
			unloadDefinitions(childDefinition, transitions);
		}
		ModuleStateChange transition = new ModuleStateChange(Transition.LOADED_TO_UNLOADED, definitionToUnload);
		transitions.add(transition);
		definitionToUnload.setState(ModuleState.UNLOADED);
	}

	protected void loadDefinitions(ModuleDefinition definitionToLoad, List<ModuleStateChange> transitions) {
		ModuleStateChange transition = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, definitionToLoad);
		transitions.add(transition);
		definitionToLoad.setState(ModuleState.LOADED);

		Collection<ModuleDefinition> childDefinitions = definitionToLoad.getChildDefinitions();
		for (ModuleDefinition childDefinition : childDefinitions) {
			loadDefinitions(childDefinition, transitions);
		}
	}
}
