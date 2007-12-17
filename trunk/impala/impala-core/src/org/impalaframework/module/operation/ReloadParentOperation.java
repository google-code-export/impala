package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;

public class ReloadParentOperation extends LoadParentOperation {

	public ReloadParentOperation(ModuleManagementSource factory, ModuleDefinitionSource pluginSpecBuilder) {
		super(factory, pluginSpecBuilder);
	}

	@Override
	protected RootModuleDefinition getExistingParentSpec(ModuleManagementSource factory) {
		return factory.getPluginStateManager().cloneParentSpec();
	}
	
}
