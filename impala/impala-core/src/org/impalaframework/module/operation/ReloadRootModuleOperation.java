package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.RootModuleDefinition;

public class ReloadRootModuleOperation extends UpdateRootModuleOperation {

	protected ReloadRootModuleOperation(ModuleManagementFactory factory) {
		super(factory);
	}

	@Override
	protected RootModuleDefinition getExistingModuleDefinitionSource(ModuleManagementFactory factory) {
		return factory.getModuleStateHolder().cloneRootModuleDefinition();
	}
	
}
