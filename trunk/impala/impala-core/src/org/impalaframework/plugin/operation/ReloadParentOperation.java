package org.impalaframework.plugin.operation;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;

public class ReloadParentOperation extends LoadParentOperation {

	public ReloadParentOperation(ImpalaBootstrapFactory factory, PluginSpecProvider pluginSpecBuilder) {
		super(factory, pluginSpecBuilder);
	}

	@Override
	protected ParentSpec getExistingParentSpec(ImpalaBootstrapFactory factory) {
		return factory.getPluginStateManager().cloneParentSpec();
	}

	
	
}
