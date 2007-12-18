package org.impalaframework.module.transition;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;

public interface TransitionProcessor {
	public boolean process(PluginStateManager pluginStateManager, RootModuleDefinition existingSpec, RootModuleDefinition newSpec, ModuleDefinition plugin);
}
