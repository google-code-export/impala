package org.impalaframework.module.modification;

import java.util.Collection;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;

public class StickyModuleModificationCalculator extends StrictModuleModificationCalculator {

	@Override
	void compareBothNotNull(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, List<ModuleStateChange> transitions) {
		if (!newSpec.equals(originalSpec) && newSpec.containsAll(originalSpec)) {
			//newspec contains locations not in original spec
			transitions.add(new ModuleStateChange(ModuleTransition.CONTEXT_LOCATIONS_ADDED, newSpec));
			
			Collection<ModuleDefinition> newPlugins = newSpec.getPlugins();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
		else if (!newSpec.equals(originalSpec) && originalSpec.containsAll(newSpec)) {
			newSpec.addContextLocations(originalSpec);
			Collection<ModuleDefinition> newPlugins = newSpec.getPlugins();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
		else {
			super.compareBothNotNull(originalSpec, newSpec, transitions);
		}
	}
	
	@Override
	void checkOriginal(ModuleDefinition originalSpec, ModuleDefinition newSpec, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> oldPlugins = originalSpec.getPlugins();

		for (ModuleDefinition oldPlugin : oldPlugins) {
			ModuleDefinition newPlugin = newSpec.getPlugin(oldPlugin.getName());

			if (newPlugin == null) {
				newSpec.add(oldPlugin);
				oldPlugin.setParent(newSpec);
			}
		}
	}

}
