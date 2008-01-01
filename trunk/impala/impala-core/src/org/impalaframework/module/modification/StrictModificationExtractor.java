package org.impalaframework.module.modification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleState;
import org.impalaframework.module.definition.RootModuleDefinition;

public class StrictModificationExtractor implements ModificationExtractor {

	@SuppressWarnings("unchecked")
	public TransitionSet getTransitions(RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition) {

		if (originalDefinition == null && newDefinition == null) {
			throw new IllegalArgumentException("Either originalDefinition or newDefinition must be non-null");
		}

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();

		if (originalDefinition != null && newDefinition == null) {
			unloadPlugins(originalDefinition, transitions);
		}
		else if (newDefinition != null && originalDefinition == null) {
			loadPlugins(newDefinition, transitions);
		}
		else {
			compareBothNotNull(originalDefinition, newDefinition, transitions);
		}
		
		return new TransitionSet(transitions, newDefinition);
	}

	void compareBothNotNull(RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition, List<ModuleStateChange> transitions) {
		compare(originalDefinition, newDefinition, transitions);
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
		
		if (ModuleState.STALE.equals(newDefinition.getState())) {
			unloadPlugins(oldDefinition, transitions);
			loadPlugins(newDefinition, transitions);
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

	void unloadPlugins(ModuleDefinition definitionToUnload, List<ModuleStateChange> transitions) {
		Collection<ModuleDefinition> childPlugins = definitionToUnload.getChildDefinitions();
		for (ModuleDefinition childPlugin : childPlugins) {
			unloadPlugins(childPlugin, transitions);
		}
		ModuleStateChange transition = new ModuleStateChange(Transition.LOADED_TO_UNLOADED, definitionToUnload);
		transitions.add(transition);
		definitionToUnload.setState(ModuleState.UNLOADED);
	}

	void loadPlugins(ModuleDefinition definitionToLoad, List<ModuleStateChange> transitions) {
		ModuleStateChange transition = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, definitionToLoad);
		transitions.add(transition);
		definitionToLoad.setState(ModuleState.LOADED);

		Collection<ModuleDefinition> childPlugins = definitionToLoad.getChildDefinitions();
		for (ModuleDefinition childPlugin : childPlugins) {
			loadPlugins(childPlugin, transitions);
		}
	}
}
