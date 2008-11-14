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

import java.util.Collection;
import java.util.List;

import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.Transition;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;

/**
 * Extends {@link StrictModificationExtractor}, allowing existing already present module definitions
 * to be retained even if they are not present in the new {@link RootModuleDefinition} hierarchy. 
 * This is useful when running a suite of integration tests. Modules which aren't explicitly declared
 * as being used in a test can be retained based on the assumption that they may be useful in subsequent
 * tests. Prevents unnecessary unloading and reloading of modules.
 * 
 * This implementation also allows context locations to be added to the root module definition without
 * requiring the root module definition to reload.
 * @author Phil Zoio
 */
public class StickyModificationExtractor extends StrictModificationExtractor {	
	
	@Override
	protected void compareRootDefinitions(RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
		//FIXME is this not assuming that new and original will only differ if a context location has been added?
		
		if (!newDefinition.equals(originalDefinition) && newDefinition.containsAll(originalDefinition)) {
			//new definition contains locations not in original definition
			transitions.add(new ModuleStateChange(Transition.CONTEXT_LOCATIONS_ADDED, newDefinition));
			
			Collection<ModuleDefinition> newModules = newDefinition.getChildDefinitions();
			checkNew(originalDefinition, newModules, transitions);
			checkOriginal(originalDefinition, newDefinition, transitions);
		}
		else if (!newDefinition.equals(originalDefinition) && originalDefinition.containsAll(newDefinition)) {
			newDefinition.addContextLocations(originalDefinition);
			Collection<ModuleDefinition> newModules = newDefinition.getChildDefinitions();
			checkNew(originalDefinition, newModules, transitions);
			checkOriginal(originalDefinition, newDefinition, transitions);
		}
		else {
			super.compareRootDefinitions(originalDefinition, newDefinition, transitions);
		}
	}
	
	@Override
	protected void checkOriginal(ModuleDefinition originalDefinition, ModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> oldModules = originalDefinition.getChildDefinitions();

		for (ModuleDefinition oldDefinition : oldModules) {
			ModuleDefinition newDef = newDefinition.getModule(oldDefinition.getName());

			if (newDef == null) {
				newDefinition.add(oldDefinition);
				oldDefinition.setParentDefinition(newDefinition);
			}
		}
	}

}
