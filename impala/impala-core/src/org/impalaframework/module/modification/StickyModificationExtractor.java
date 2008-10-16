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

public class StickyModificationExtractor extends StrictModificationExtractor {	
	
	@Override
	void compareBothNotNull(RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
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
			super.compareBothNotNull(originalDefinition, newDefinition, transitions);
		}
	}
	
	@Override
	void checkOriginal(ModuleDefinition originalDefinition, ModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
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
