package org.impalaframework.plugin.spec.modification;

import org.impalaframework.plugin.spec.PluginSpec;

public class PluginStateChange {
	private final PluginTransition transition;

	private final PluginSpec pluginSpec;

	public PluginStateChange(PluginTransition transition, PluginSpec pluginSpec) {
		super();
		//FIXME add assert
		this.transition = transition;
		this.pluginSpec = pluginSpec;
	}

	public PluginSpec getPluginSpec() {
		return pluginSpec;
	}

	public PluginTransition getTransition() {
		return transition;
	}

}
