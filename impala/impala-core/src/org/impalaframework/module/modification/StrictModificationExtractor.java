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

	@SuppressWarnings("unchecked")
	public TransitionSet reload(RootModuleDefinition originalSpec, RootModuleDefinition rootDefinition, String moduleName) {
		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
		
		String name = null;
		ModuleDefinition newPlugin = rootDefinition.findChildDefinition(moduleName, true);
		
		if (newPlugin != null) {
			name = newPlugin.getName();
		}
		
		ModuleDefinition originalPlugin = null;
		
		if (name != null) 
			originalPlugin = originalSpec.findChildDefinition(name, true);
		else
			originalPlugin = originalSpec.findChildDefinition(moduleName, true);
		
		if (originalPlugin != null) {
			unloadPlugins(originalPlugin, transitions);
		}
		if (newPlugin != null) {
			loadPlugins(newPlugin, transitions);
		}
		
		return new TransitionSet(transitions, rootDefinition);
	}
	
	
	void compare(ModuleDefinition oldDefinition, ModuleDefinition newDefinition, List<ModuleStateChange> transitions) {

		boolean areEqual = !oldDefinition.equals(newDefinition);
		
		// original and new are both not null
		if (areEqual) {
			unloadPlugins(oldDefinition, transitions);
			loadPlugins(newDefinition, transitions);
		}
		else {
			Collection<ModuleDefinition> newDefinitions = newDefinition.getChildDefinitions();
			checkNew(oldDefinition, newDefinitions, transitions);
			checkOriginal(oldDefinition, newDefinition, transitions);
		}
	}

	void checkNew(ModuleDefinition originalSpec, Collection<ModuleDefinition> definitions, List<ModuleStateChange> transitions) {
		for (ModuleDefinition definition : definitions) {
			ModuleDefinition oldDefinition = originalSpec.getModule(definition.getName());

			if (oldDefinition == null) {
				ModuleStateChange transition = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, definition);
				transitions.add(transition);
			}
			else {
				compare(oldDefinition, definition, transitions);
			}
		}
	}

	void checkOriginal(ModuleDefinition originalDefinition, ModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> oldDefinitions = originalDefinition.getChildDefinitions();
		
		for (ModuleDefinition oldDefinition : oldDefinitions) {
			ModuleDefinition newPlugin = newDefinition.getModule(oldDefinition.getName());

			if (newPlugin == null) {
				unloadPlugins(oldDefinition, transitions);
			}
		}
	}

	void unloadPlugins(ModuleDefinition plugin, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> childPlugins = plugin.getChildDefinitions();
		for (ModuleDefinition childPlugin : childPlugins) {
			unloadPlugins(childPlugin, transitions);
		}
		ModuleStateChange transition = new ModuleStateChange(Transition.LOADED_TO_UNLOADED, plugin);
		transitions.add(transition);
	}

	void loadPlugins(ModuleDefinition plugin, List<ModuleStateChange> transitions) {
		ModuleStateChange transition = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, plugin);
		transitions.add(transition);

		Collection<ModuleDefinition> childPlugins = plugin.getChildDefinitions();
		for (ModuleDefinition childPlugin : childPlugins) {
			loadPlugins(childPlugin, transitions);
		}
	}
}
