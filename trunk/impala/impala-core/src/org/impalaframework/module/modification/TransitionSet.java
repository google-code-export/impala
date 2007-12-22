package org.impalaframework.module.modification;

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.module.definition.RootModuleDefinition;

public class TransitionSet {

	private Collection<? extends ModuleStateChange> moduleTransitions = new ArrayList<ModuleStateChange>();

	private RootModuleDefinition newDefinition;

	public TransitionSet(Collection<? extends ModuleStateChange> transitions, RootModuleDefinition newDefinition) {
		super();
		this.moduleTransitions = transitions;
		this.newDefinition = newDefinition;
	}

	public RootModuleDefinition getNewRootModuleDefinition() {
		return newDefinition;
	}

	public Collection<? extends ModuleStateChange> getModuleTransitions() {
		return moduleTransitions;
	}

}
