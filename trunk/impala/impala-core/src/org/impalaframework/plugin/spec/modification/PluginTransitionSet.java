package org.impalaframework.plugin.spec.modification;

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.plugin.spec.ParentSpec;

public class PluginTransitionSet {

	private Collection<? extends PluginTransition> pluginTransitions = new ArrayList<PluginTransition>();

	private ParentSpec newSpec;

	public PluginTransitionSet(Collection<? extends PluginTransition> pluginTransitions, ParentSpec newSpec) {
		super();
		this.pluginTransitions = pluginTransitions;
		this.newSpec = newSpec;
	}

	public ParentSpec getNewSpec() {
		return newSpec;
	}

	public Collection<? extends PluginTransition> getPluginTransitions() {
		return pluginTransitions;
	}

}
