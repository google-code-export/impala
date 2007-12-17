package org.impalaframework.module.transition;

import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;

public interface TransitionProcessor {
	public boolean process(PluginStateManager pluginStateManager, RootModuleDefinition existingSpec, RootModuleDefinition newSpec, ModuleDefinition plugin);
}
