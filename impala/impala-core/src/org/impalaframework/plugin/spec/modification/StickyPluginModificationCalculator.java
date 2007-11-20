package org.impalaframework.plugin.spec.modification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;

public class StickyPluginModificationCalculator extends PluginModificationCalculator {

	@Override
	public PluginTransitionSet getTransitions(ParentSpec originalSpec, ParentSpec newSpec) {
		//FIXME test
		List<PluginStateChange> transitions = new ArrayList<PluginStateChange>();

		if (originalSpec != null && newSpec != null && !originalSpec.containsAll(newSpec)) {
			transitions.add(new PluginStateChange(PluginTransition.CONTEXT_LOCATIONS_ADDED, newSpec));
			Collection<PluginSpec> newPlugins = newSpec.getPlugins();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
		else if (originalSpec != null && newSpec != null && !newSpec.containsAll(originalSpec)) {
			Collection<PluginSpec> newPlugins = newSpec.getPlugins();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
		else {
			if (originalSpec != null && newSpec == null) {
				unloadPlugins(originalSpec, transitions);
			}
			else if (newSpec != null && originalSpec == null) {
				loadPlugins(newSpec, transitions);
			}
			else {
				compare(originalSpec, newSpec, transitions);
			}
		}
		return new PluginTransitionSet(transitions, newSpec);
	}

	@Override
	void checkOriginal(PluginSpec originalSpec, PluginSpec newSpec, List<PluginStateChange> transitions) {
		Collection<PluginSpec> oldPlugins = originalSpec.getPlugins();

		for (PluginSpec oldPlugin : oldPlugins) {
			PluginSpec newPlugin = newSpec.getPlugin(oldPlugin.getName());

			if (newPlugin == null) {
				// FIXME add test
				newSpec.add(oldPlugin);
				oldPlugin.setParent(newSpec);
			}
		}
	}

}
