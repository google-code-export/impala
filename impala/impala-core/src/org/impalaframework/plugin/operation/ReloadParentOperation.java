package org.impalaframework.plugin.operation;

import org.impalaframework.plugin.bootstrap.ModuleManagementSource;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;

public class ReloadParentOperation extends LoadParentOperation {

	public ReloadParentOperation(ModuleManagementSource factory, PluginSpecProvider pluginSpecBuilder) {
		super(factory, pluginSpecBuilder);
	}

	@Override
	protected ParentSpec getExistingParentSpec(ModuleManagementSource factory) {
		return factory.getPluginStateManager().cloneParentSpec();
	}
	
}
