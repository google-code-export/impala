package org.impalaframework.module.modification;

import java.util.Collection;
import java.util.List;

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
