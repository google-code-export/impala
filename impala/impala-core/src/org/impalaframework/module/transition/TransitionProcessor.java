package org.impalaframework.module.transition;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;

public interface TransitionProcessor {
	//FIXME reconsider this interface
	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition newRootDefinition, ModuleDefinition currentModuleDefinition);
}
