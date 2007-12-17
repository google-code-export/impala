package org.impalaframework.module.modification;

import java.util.Collection;
import java.util.List;

import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;

public class StickyPluginModificationCalculator extends StrictPluginModificationCalculator {

	@Override
	void compareBothNotNull(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, List<PluginStateChange> transitions) {
		if (!newSpec.equals(originalSpec) && newSpec.containsAll(originalSpec)) {
			//newspec contains locations not in original spec
			transitions.add(new PluginStateChange(PluginTransition.CONTEXT_LOCATIONS_ADDED, newSpec));
			
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
	void checkOriginal(ModuleDefinition originalSpec, ModuleDefinition newSpec, List<PluginStateChange> transitions) {
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
