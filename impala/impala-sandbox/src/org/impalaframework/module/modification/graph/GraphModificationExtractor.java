package org.impalaframework.module.modification.graph;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.ModificationExtractor;
import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.RootModuleDefinition;

//FIXME implement - just skeleton here
public class GraphModificationExtractor implements ModificationExtractor {

	public TransitionSet getTransitions(
			RootModuleDefinition originalDefinition,
			RootModuleDefinition newDefinition) {

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
		
		if (originalDefinition != null && newDefinition == null) {
			unloadDefinitions(originalDefinition, transitions);
		}
		else if (newDefinition != null && originalDefinition == null) {
			loadDefinitions(newDefinition, transitions);
		}
		else {
			compareBothNotNull(originalDefinition, newDefinition, transitions);
		}
		
		return new TransitionSet(transitions, newDefinition);
	}

	private void compareBothNotNull(RootModuleDefinition originalDefinition,
			RootModuleDefinition newDefinition,
			List<ModuleStateChange> transitions) {
	}

	private void loadDefinitions(RootModuleDefinition newDefinition,
			List<ModuleStateChange> transitions) {
	}

	private void unloadDefinitions(RootModuleDefinition originalDefinition,
			List<ModuleStateChange> transitions) {
	}

}
