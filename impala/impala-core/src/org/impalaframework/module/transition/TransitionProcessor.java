package org.impalaframework.module.transition;

import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpec;

public interface TransitionProcessor {
	public boolean process(PluginStateManager pluginStateManager, ParentSpec existingSpec, ParentSpec newSpec, PluginSpec plugin);
}
