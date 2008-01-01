package org.impalaframework.module.operation;

import org.impalaframework.module.definition.RootModuleDefinition;

public class ReloadRootModuleOperation extends UpdateRootModuleOperation {

	protected ReloadRootModuleOperation() {
		super();
	}

	@Override
	protected RootModuleDefinition getExistingModuleDefinitionSource() {
		return getModuleStateHolder().cloneRootModuleDefinition();
	}
	
}
