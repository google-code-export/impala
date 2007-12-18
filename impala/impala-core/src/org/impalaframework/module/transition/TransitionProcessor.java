package org.impalaframework.module.transition;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;

public interface TransitionProcessor {
	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition existingSpec, RootModuleDefinition newSpec, ModuleDefinition plugin);
}
