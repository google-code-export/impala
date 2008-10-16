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

public class StrictModificationExtractor implements ModificationExtractor {

	@SuppressWarnings("unchecked")
	public TransitionSet getTransitions(RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition) {

		if (originalDefinition == null && newDefinition == null) {
			throw new IllegalArgumentException("Either originalDefinition or newDefinition must be non-null");
		}

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();

		if (originalDefinition != null && newDefinition == null) {
			unloadDefinitions(originalDefinition, transitions);
		}
		else if (newDefinition != null && originalDefinition == null) {
			loadDefinitions(newDefinition, transitions);
		}
		else {
			compareBothNotNull(originalDefinition, newDefinition, transitions);
		}
		
		return new TransitionSet(transitions, newDefinition);
	}

	void compareBothNotNull(RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
		compare(originalDefinition, newDefinition, transitions);
	}
	
	void compare(ModuleDefinition oldDefinition, ModuleDefinition newDefinition, List<ModuleStateChange> transitions) {

		boolean areEqual = !oldDefinition.equals(newDefinition);
		
		// original and new are both not null
		if (areEqual) {
			unloadDefinitions(oldDefinition, transitions);
			loadDefinitions(newDefinition, transitions);
		}
		else {
			Collection<ModuleDefinition> newDefinitions = newDefinition.getChildDefinitions();
			checkNew(oldDefinition, newDefinitions, transitions);
			checkOriginal(oldDefinition, newDefinition, transitions);
		}
		
		if (ModuleState.STALE.equals(newDefinition.getState())) {
			unloadDefinitions(oldDefinition, transitions);
			loadDefinitions(newDefinition, transitions);
		}
	}

	void checkNew(ModuleDefinition originalDefinition, Collection<ModuleDefinition> definitions, List<ModuleStateChange> transitions) {
		for (ModuleDefinition definition : definitions) {
			ModuleDefinition oldDefinition = originalDefinition.getModule(definition.getName());

			if (oldDefinition == null) {
				loadDefinitions(definition, transitions);				
			}
			else {
				compare(oldDefinition, definition, transitions);
			}
		}
	}

	void checkOriginal(ModuleDefinition originalDefinition, ModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> oldDefinitions = originalDefinition.getChildDefinitions();
		
		for (ModuleDefinition oldDefinition : oldDefinitions) {
			ModuleDefinition newDef = newDefinition.getModule(oldDefinition.getName());

			if (newDef == null) {
				unloadDefinitions(oldDefinition, transitions);
			}
		}
	}

	void unloadDefinitions(ModuleDefinition definitionToUnload, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> childDefinitions = definitionToUnload.getChildDefinitions();
		for (ModuleDefinition childDefinition : childDefinitions) {
			unloadDefinitions(childDefinition, transitions);
		}
		ModuleStateChange transition = new ModuleStateChange(Transition.LOADED_TO_UNLOADED, definitionToUnload);
		transitions.add(transition);
		definitionToUnload.setState(ModuleState.UNLOADED);
	}

	void loadDefinitions(ModuleDefinition definitionToLoad, List<ModuleStateChange> transitions) {
		ModuleStateChange transition = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, definitionToLoad);
		transitions.add(transition);
		definitionToLoad.setState(ModuleState.LOADED);

		Collection<ModuleDefinition> childDefinitions = definitionToLoad.getChildDefinitions();
		for (ModuleDefinition childDefinition : childDefinitions) {
			loadDefinitions(childDefinition, transitions);
		}
	}
}
