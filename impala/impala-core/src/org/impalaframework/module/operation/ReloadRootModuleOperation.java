package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;

public class ReloadRootModuleOperation extends UpdateRootModuleOperation {

	public ReloadRootModuleOperation(ModuleManagementFactory factory, ModuleDefinitionSource moduleDefinitionSource) {
		super(factory, moduleDefinitionSource);
	}

	@Override
	protected RootModuleDefinition getExistingParentSpec(ModuleManagementFactory factory) {
		return factory.getModuleStateHolder().cloneRootModuleDefinition();
	}
	
}
