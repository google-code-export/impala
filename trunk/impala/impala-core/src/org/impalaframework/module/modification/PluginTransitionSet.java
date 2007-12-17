package org.impalaframework.module.modification;

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.module.spec.RootModuleDefinition;

public class PluginTransitionSet {

	private Collection<? extends PluginStateChange> pluginTransitions = new ArrayList<PluginStateChange>();

	private RootModuleDefinition newSpec;

	public PluginTransitionSet(Collection<? extends PluginStateChange> pluginTransitions, RootModuleDefinition newSpec) {
		super();
		this.pluginTransitions = pluginTransitions;
		this.newSpec = newSpec;
	}

	public RootModuleDefinition getNewSpec() {
		return newSpec;
	}

	public Collection<? extends PluginStateChange> getPluginTransitions() {
		return pluginTransitions;
	}

}
