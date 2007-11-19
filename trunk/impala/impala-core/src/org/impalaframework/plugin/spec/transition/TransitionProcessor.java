package org.impalaframework.plugin.spec.transition;

import org.impalaframework.plugin.spec.PluginSpec;

public interface TransitionProcessor {
	public void process(PluginStateManager pluginStateManager, PluginSpec plugin);
}
