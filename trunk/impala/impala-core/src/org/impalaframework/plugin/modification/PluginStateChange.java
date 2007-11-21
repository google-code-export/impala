package org.impalaframework.plugin.modification;

import org.impalaframework.plugin.spec.PluginSpec;
import org.springframework.util.Assert;

public final class PluginStateChange {
	
	private final PluginTransition transition;

	private final PluginSpec pluginSpec;

	public PluginStateChange(PluginTransition transition, PluginSpec pluginSpec) {
		super();
		Assert.notNull(transition);
		Assert.notNull(pluginSpec);
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
