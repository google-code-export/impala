package org.impalaframework.module.modification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;

public class StrictModificationExtractor implements ModificationExtractor {

	@SuppressWarnings("unchecked")
	public TransitionSet getTransitions(RootModuleDefinition originalSpec, RootModuleDefinition newSpec) {

		if (originalSpec == null && newSpec == null) {
			throw new IllegalArgumentException("Either originalSpec or newSpec must be non-null");
		}

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();

		if (originalSpec != null && newSpec == null) {
			unloadPlugins(originalSpec, transitions);
		}
		else if (newSpec != null && originalSpec == null) {
			loadPlugins(newSpec, transitions);
		}
		else {
			compareBothNotNull(originalSpec, newSpec, transitions);
		}
		
		return new TransitionSet(transitions, newSpec);
	}

	void compareBothNotNull(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, List<ModuleStateChange> transitions) {
		compare(originalSpec, newSpec, transitions);
	}

	public TransitionSet reloadLike(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, String pluginToReload) {
		return reload(originalSpec, newSpec, pluginToReload, false);
	}

	@SuppressWarnings("unchecked")
	public TransitionSet reload(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, String pluginToReload) {
		return reload(originalSpec, newSpec, pluginToReload, true);
	}
	
	
	@SuppressWarnings("unchecked")
	public TransitionSet reload(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, String pluginToReload, boolean exactMatch) {

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();

		String name = null;
		ModuleDefinition newPlugin = newSpec.findChildDefinition(pluginToReload, exactMatch);
		
		if (newPlugin != null) {
			name = newPlugin.getName();
		}
		
		ModuleDefinition originalPlugin = null;
		
		if (name != null) 
			originalPlugin = originalSpec.findChildDefinition(name, true);
		else
			originalPlugin = originalSpec.findChildDefinition(pluginToReload, exactMatch);
		
		if (originalPlugin != null) {
			unloadPlugins(originalPlugin, transitions);
		}
		if (newPlugin != null) {
			loadPlugins(newPlugin, transitions);
		}
		
		return new TransitionSet(transitions, newSpec);
	}

	void compare(ModuleDefinition originalSpec, ModuleDefinition newSpec, List<ModuleStateChange> transitions) {

		boolean areEqual = !originalSpec.equals(newSpec);
		
		// original and new are both not null
		if (areEqual) {
			unloadPlugins(originalSpec, transitions);
			loadPlugins(newSpec, transitions);
		}
		else {
			Collection<ModuleDefinition> newPlugins = newSpec.getModules();
			checkNew(originalSpec, newPlugins, transitions);
			checkOriginal(originalSpec, newSpec, transitions);
		}
	}

	void checkNew(ModuleDefinition originalSpec, Collection<ModuleDefinition> newPlugins, List<ModuleStateChange> transitions) {
		for (ModuleDefinition newPlugin : newPlugins) {
			ModuleDefinition oldPlugin = originalSpec.getModule(newPlugin.getName());

			if (oldPlugin == null) {
				ModuleStateChange transition = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, newPlugin);
				transitions.add(transition);
			}
			else {
				compare(oldPlugin, newPlugin, transitions);
			}
		}
	}

	void checkOriginal(ModuleDefinition originalSpec, ModuleDefinition newSpec, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> oldPlugins = originalSpec.getModules();
		
		for (ModuleDefinition oldPlugin : oldPlugins) {
			ModuleDefinition newPlugin = newSpec.getModule(oldPlugin.getName());

			if (newPlugin == null) {
				unloadPlugins(oldPlugin, transitions);
			}
		}
	}

	void unloadPlugins(ModuleDefinition plugin, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> childPlugins = plugin.getModules();
		for (ModuleDefinition childPlugin : childPlugins) {
			unloadPlugins(childPlugin, transitions);
		}
		ModuleStateChange transition = new ModuleStateChange(Transition.LOADED_TO_UNLOADED, plugin);
		transitions.add(transition);
	}

	void loadPlugins(ModuleDefinition plugin, List<ModuleStateChange> transitions) {
		ModuleStateChange transition = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, plugin);
		transitions.add(transition);

		Collection<ModuleDefinition> childPlugins = plugin.getModules();
		for (ModuleDefinition childPlugin : childPlugins) {
			loadPlugins(childPlugin, transitions);
		}
	}
}
