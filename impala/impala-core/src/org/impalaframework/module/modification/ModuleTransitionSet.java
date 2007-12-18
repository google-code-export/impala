package org.impalaframework.module.modification;

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.module.definition.RootModuleDefinition;

public class ModuleTransitionSet {

	private Collection<? extends ModuleStateChange> pluginTransitions = new ArrayList<ModuleStateChange>();

	private RootModuleDefinition newSpec;

	public ModuleTransitionSet(Collection<? extends ModuleStateChange> pluginTransitions, RootModuleDefinition newSpec) {
		super();
		this.pluginTransitions = pluginTransitions;
		this.newSpec = newSpec;
	}

	public RootModuleDefinition getNewSpec() {
		return newSpec;
	}

	public Collection<? extends ModuleStateChange> getPluginTransitions() {
		return pluginTransitions;
	}

}
