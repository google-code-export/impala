package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;

public class ReloadRootModuleOperation extends UpdateRootModuleOperation {

	public ReloadRootModuleOperation(ModuleManagementSource factory, ModuleDefinitionSource moduleDefinitionSource) {
		super(factory, moduleDefinitionSource);
	}

	@Override
	protected RootModuleDefinition getExistingParentSpec(ModuleManagementSource factory) {
		return factory.getModuleStateHolder().cloneRootModuleDefinition();
	}
	
}
