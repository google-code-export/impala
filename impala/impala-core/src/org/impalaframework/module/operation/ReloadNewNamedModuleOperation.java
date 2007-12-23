package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.util.Assert;

public class ReloadNewNamedModuleOperation extends ReloadNamedModuleOperation {


	public ReloadNewNamedModuleOperation(ModuleManagementFactory factory) {
		super(factory);
	}

	@Override
	protected RootModuleDefinition newRootModuleDefinition(ModuleOperationInput moduleOperationInput) {
		ModuleDefinitionSource moduleDefinitionSource = moduleOperationInput.getModuleDefinitionSource();
		
		//FIXME check that this is not null
		Assert.notNull(moduleDefinitionSource);
		
		return moduleDefinitionSource.getModuleDefinition();
	}

}
