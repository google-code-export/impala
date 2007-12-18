package org.impalaframework.module.transition;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateManager;

public interface TransitionProcessor {
	public boolean process(ModuleStateManager moduleStateManager, RootModuleDefinition existingSpec, RootModuleDefinition newSpec, ModuleDefinition plugin);
}
