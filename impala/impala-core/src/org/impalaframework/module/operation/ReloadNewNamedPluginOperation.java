package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpecProvider;
import org.springframework.util.Assert;

public class ReloadNewNamedPluginOperation extends ReloadNamedPluginOperation {

	private PluginSpecProvider pluginSpecProvider;

	public ReloadNewNamedPluginOperation(ModuleManagementSource factory, String pluginName,
			PluginSpecProvider pluginSpecProvider) {
		super(factory, pluginName);
		Assert.notNull(pluginSpecProvider);
		this.pluginSpecProvider = pluginSpecProvider;
	}

	@Override
	protected ParentSpec newPluginSpec() {
		return pluginSpecProvider.getPluginSpec();
	}

}
