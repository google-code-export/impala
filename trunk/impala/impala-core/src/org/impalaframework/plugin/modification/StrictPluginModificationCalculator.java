package org.impalaframework.plugin.modification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;

public class StrictPluginModificationCalculator {

	@SuppressWarnings("unchecked")
	public PluginTransitionSet getTransitions(ParentSpec originalSpec, ParentSpec newSpec) {

		if (originalSpec == null && newSpec == null) {
			throw new IllegalArgumentException("Either originalSpec or newSpec must be non-null");
		}

		List<PluginStateChange> transitions = new ArrayList<PluginStateChange>();

		if (originalSpec != null && newSpec == null) {
			unloadPlugins(originalSpec, transitions);
		}
		else if (newSpec != null && originalSpec == null) {
			loadPlugins(newSpec, transitions);
		}
		else {
			compareBothNotNull(originalSpec, newSpec, transitions);
		}
		
		return new PluginTransitionSet(transitions, newSpec);
	}

	void compareBothNotNull(ParentSpec originalSpec, ParentSpec newSpec, List<PluginStateChange> transitions) {
		compare(originalSpec, newSpec, transitions);
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

	void compare(PluginSpec originalSpec, PluginSpec newSpec, List<PluginStateChange> transitions) {

		boolean areEqual = !originalSpec.equals(newSpec);
		
		// original and new are both not null
		if (areEqual) {
			unloadPlugins(originalSpec, transitions);
			loadPlugins(newSpec, transitions);
		}
		else {
			Collection<PluginSpec> newPlugins = newSpec.getPlugins();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
	}

	void checkNew(PluginSpec originalSpec, Collection<PluginSpec> newPlugins, List<PluginStateChange> transitions) {
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
	}

	void checkOriginal(PluginSpec originalSpec, PluginSpec newSpec, List<PluginStateChange> transitions) {
		Collection<PluginSpec> oldPlugins = originalSpec.getPlugins();
		
		for (PluginSpec oldPlugin : oldPlugins) {
			PluginSpec newPlugin = newSpec.getPlugin(oldPlugin.getName());

			if (newPlugin == null) {
				unloadPlugins(oldPlugin, transitions);
			}
		}
	}

	void unloadPlugins(PluginSpec plugin, List<PluginStateChange> transitions) {
		Collection<PluginSpec> childPlugins = plugin.getPlugins();
		for (PluginSpec childPlugin : childPlugins) {
			unloadPlugins(childPlugin, transitions);
		}
		PluginStateChange transition = new PluginStateChange(PluginTransition.LOADED_TO_UNLOADED, plugin);
		transitions.add(transition);
	}

	void loadPlugins(PluginSpec plugin, List<PluginStateChange> transitions) {
		PluginStateChange transition = new PluginStateChange(PluginTransition.UNLOADED_TO_LOADED, plugin);
		transitions.add(transition);

		Collection<PluginSpec> childPlugins = plugin.getPlugins();
		for (PluginSpec childPlugin : childPlugins) {
			loadPlugins(childPlugin, transitions);
		}
	}
}
