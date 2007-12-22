package org.impalaframework.module.modification;

import java.util.Collection;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;

public class StickyModificationExtractor extends StrictModificationExtractor {

	@Override
	void compareBothNotNull(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, List<ModuleStateChange> transitions) {
		if (!newSpec.equals(originalSpec) && newSpec.containsAll(originalSpec)) {
			//newspec contains locations not in original spec
			transitions.add(new ModuleStateChange(Transition.CONTEXT_LOCATIONS_ADDED, newSpec));
			
			Collection<ModuleDefinition> newPlugins = newSpec.getChildDefinitions();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
		else if (!newSpec.equals(originalSpec) && originalSpec.containsAll(newSpec)) {
			newSpec.addContextLocations(originalSpec);
			Collection<ModuleDefinition> newPlugins = newSpec.getChildDefinitions();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
		else {
			super.compareBothNotNull(originalSpec, newSpec, transitions);
		}
	}
	
	@Override
	void checkOriginal(ModuleDefinition originalSpec, ModuleDefinition newSpec, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> oldPlugins = originalSpec.getChildDefinitions();

		for (ModuleDefinition oldPlugin : oldPlugins) {
			ModuleDefinition newPlugin = newSpec.getModule(oldPlugin.getName());

			if (newPlugin == null) {
				newSpec.add(oldPlugin);
				oldPlugin.setParentDefinition(newSpec);
			}
		}
	}

}
