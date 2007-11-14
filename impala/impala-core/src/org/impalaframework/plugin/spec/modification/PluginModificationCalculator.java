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
		else if (originalSpec != null && newSpec == null) {
			unloadPlugins(originalSpec, transitions);
		}
		else if (newSpec != null && originalSpec == null) {
			loadPlugins(newSpec, transitions);
		}
		else {
			compare(originalSpec, newSpec, transitions);
		}
		
		return new PluginTransitionSet(transitions, newSpec);
	}

	public PluginTransitionSet reloadLike(ParentSpec originalSpec, ParentSpec newSpec, String pluginToReload) {
		return reload(originalSpec, newSpec, pluginToReload, false);
	}

	@SuppressWarnings("unchecked")
	public PluginTransitionSet reload(ParentSpec originalSpec, ParentSpec newSpec, String pluginToReload) {
		return reload(originalSpec, newSpec, pluginToReload, true);
	}
	
	
	@SuppressWarnings("unchecked")
	public PluginTransitionSet reload(ParentSpec originalSpec, ParentSpec newSpec, String pluginToReload, boolean exactMatch) {

		List<PluginStateChange> transitions = new ArrayList<PluginStateChange>();

		String name = null;
		PluginSpec newPlugin = newSpec.findPlugin(pluginToReload, exactMatch);
		
		if (newPlugin != null) {
			name = newPlugin.getName();
		}
		
		PluginSpec originalPlugin = null;
		
		if (name != null) 
			originalPlugin = originalSpec.findPlugin(name, true);
		else
			originalPlugin = originalSpec.findPlugin(pluginToReload, exactMatch);
		
		if (originalPlugin != null) {
			unloadPlugins(originalPlugin, transitions);
		}
		if (newPlugin != null) {
			loadPlugins(newPlugin, transitions);
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
			Collection<PluginSpec> newPlugins = newSpec.getPlugins();
			for (PluginSpec newPlugin : newPlugins) {
				PluginSpec oldPlugin = originalSpec.getPlugin(newPlugin.getName());

				if (oldPlugin == null) {
					PluginStateChange transition = new PluginStateChange(PluginTransition.UNLOADED_TO_LOADED, newPlugin);
					transitions.add(transition);
				}
				else {
					compare(oldPlugin, newPlugin, transitions);
				}
			}
			
			//FIXME unit test this 
			Collection<PluginSpec> oldPlugins = originalSpec.getPlugins();
			
			for (PluginSpec oldPlugin : oldPlugins) {
				PluginSpec newPlugin = newSpec.getPlugin(oldPlugin.getName());

				if (newPlugin == null) {
					unloadPlugins(oldPlugin, transitions);
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
