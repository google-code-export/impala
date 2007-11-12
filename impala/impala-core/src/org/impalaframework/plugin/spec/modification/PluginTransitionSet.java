package org.impalaframework.plugin.spec.modification;

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.plugin.spec.ParentSpec;

public class PluginTransitionSet {

	private Collection<? extends PluginStateChange> pluginTransitions = new ArrayList<PluginStateChange>();

	private ParentSpec newSpec;

	public PluginTransitionSet(Collection<? extends PluginStateChange> pluginTransitions, ParentSpec newSpec) {
		super();
		this.pluginTransitions = pluginTransitions;
		this.newSpec = newSpec;
	}

	public ParentSpec getNewSpec() {
		return newSpec;
	}

	public Collection<? extends PluginStateChange> getPluginTransitions() {
		return pluginTransitions;
	}

}
