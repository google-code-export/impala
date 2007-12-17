package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpecProvider;

public class ReloadParentOperation extends LoadParentOperation {

	public ReloadParentOperation(ModuleManagementSource factory, PluginSpecProvider pluginSpecBuilder) {
		super(factory, pluginSpecBuilder);
	}

	@Override
	protected ParentSpec getExistingParentSpec(ModuleManagementSource factory) {
		return factory.getPluginStateManager().cloneParentSpec();
	}
	
}
