package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.util.Assert;

public class ReloadNewNamedModuleOperation extends ReloadNamedModuleOperation {

	private ModuleDefinitionSource moduleDefinitionSource;

	public ReloadNewNamedModuleOperation(ModuleManagementSource factory, String moduleName,
			ModuleDefinitionSource moduleDefinitionSource) {
		super(factory, moduleName);
		Assert.notNull(moduleDefinitionSource);
		this.moduleDefinitionSource = moduleDefinitionSource;
	}

	@Override
	protected RootModuleDefinition newRootModuleDefinition() {
		return moduleDefinitionSource.getModuleDefinition();
	}

}
