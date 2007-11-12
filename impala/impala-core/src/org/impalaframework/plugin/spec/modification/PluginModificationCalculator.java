package org.impalaframework.plugin.spec.modification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;

public class PluginModificationCalculator {

	@SuppressWarnings("unchecked")
	public PluginTransitionSet getTransitions(ParentSpec originalSpec, ParentSpec newSpec) {

		List<PluginStateChange> transitions = new ArrayList<PluginStateChange>();

		if (originalSpec == null && newSpec == null) {
			// FIXME test
			// return new PluginTransitionSet(Collections.EMPTY_LIST, null);
		}
		if (originalSpec != null && newSpec == null) {
			unloadPlugins(originalSpec, transitions);
		}
		if (newSpec != null && originalSpec == null) {
			loadPlugins(newSpec, transitions);
		}
		else {
			compare(originalSpec, newSpec, transitions);
		}
		
		return new PluginTransitionSet(transitions, newSpec);
	}

	private void compare(PluginSpec originalSpec, PluginSpec newSpec, List<PluginStateChange> transitions) {

		// original and parent are both not null
		if (!originalSpec.equals(newSpec)) {
			unloadPlugins(originalSpec, transitions);
			loadPlugins(newSpec, transitions);
		}
		else {
			Collection<PluginSpec> plugins = newSpec.getPlugins();
			for (PluginSpec newPlugin : plugins) {
				PluginSpec oldPlugin = originalSpec.getPlugin(newPlugin.getName());

				if (oldPlugin == null) {
					PluginStateChange transition = new PluginStateChange(PluginTransition.UNLOADED_TO_LOADED, newPlugin);
					transitions.add(transition);
				}
				else {
					compare(oldPlugin, newPlugin, transitions);
				}
			}
		}
	}

	private void unloadPlugins(PluginSpec plugin, List<PluginStateChange> transitions) {
		Collection<PluginSpec> childPlugins = plugin.getPlugins();
		for (PluginSpec childPlugin : childPlugins) {
			unloadPlugins(childPlugin, transitions);
		}
		PluginStateChange transition = new PluginStateChange(PluginTransition.LOADED_TO_UNLOADED, plugin);
		transitions.add(transition);
	}

	private void loadPlugins(PluginSpec plugin, List<PluginStateChange> transitions) {
		PluginStateChange transition = new PluginStateChange(PluginTransition.UNLOADED_TO_LOADED, plugin);
		transitions.add(transition);

		Collection<PluginSpec> childPlugins = plugin.getPlugins();
		for (PluginSpec childPlugin : childPlugins) {
			loadPlugins(childPlugin, transitions);
		}
	}
}
