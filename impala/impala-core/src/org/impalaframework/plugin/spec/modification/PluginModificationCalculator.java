package org.impalaframework.plugin.spec.modification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;

public class PluginModificationCalculator {

	@SuppressWarnings("unchecked")
	public PluginTransitionSet getTransitions(ParentSpec originalSpec, ParentSpec newSpec) {

		if (originalSpec == null && newSpec == null) {
			return new PluginTransitionSet(Collections.EMPTY_LIST, null);
		}
		if (originalSpec != null && newSpec == null) {
			return new PluginTransitionSet(Collections.EMPTY_LIST, originalSpec);
		}
		if (newSpec != null && originalSpec == null) {
			return new PluginTransitionSet(Collections.EMPTY_LIST, newSpec);
		}
		
		List<PluginStateChange> toRemove = new ArrayList<PluginStateChange>();

		// original and parent are both not null
		if (!originalSpec.equals(newSpec)) {
			unloadPlugins(originalSpec, toRemove);
			loadPlugins(newSpec, toRemove);
		}
	
		return new PluginTransitionSet(toRemove, newSpec);
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
