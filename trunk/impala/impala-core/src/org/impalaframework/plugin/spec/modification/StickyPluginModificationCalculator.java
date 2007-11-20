package org.impalaframework.plugin.spec.modification;

import java.util.Collection;
import java.util.List;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;

public class StickyPluginModificationCalculator extends PluginModificationCalculator {

	@Override
	void compareBothNotNull(ParentSpec originalSpec, ParentSpec newSpec, List<PluginStateChange> transitions) {
		if (!newSpec.equals(originalSpec) && newSpec.containsAll(originalSpec)) {
			//newspec contains locations not in original spec
			transitions.add(new PluginStateChange(PluginTransition.CONTEXT_LOCATIONS_ADDED, newSpec));
			
			Collection<PluginSpec> newPlugins = newSpec.getPlugins();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
		else if (!newSpec.equals(originalSpec) && originalSpec.containsAll(newSpec)) {
			newSpec.addContextLocations(originalSpec);
			Collection<PluginSpec> newPlugins = newSpec.getPlugins();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
		else {
			super.compareBothNotNull(originalSpec, newSpec, transitions);
		}
	}
	
	@Override
	void checkOriginal(PluginSpec originalSpec, PluginSpec newSpec, List<PluginStateChange> transitions) {
		Collection<PluginSpec> oldPlugins = originalSpec.getPlugins();

		for (PluginSpec oldPlugin : oldPlugins) {
			PluginSpec newPlugin = newSpec.getPlugin(oldPlugin.getName());

			if (newPlugin == null) {
				newSpec.add(oldPlugin);
				oldPlugin.setParent(newSpec);
			}
		}
	}

}
