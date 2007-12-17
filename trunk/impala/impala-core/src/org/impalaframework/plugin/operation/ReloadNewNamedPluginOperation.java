package org.impalaframework.plugin.operation;

import org.impalaframework.plugin.bootstrap.ModuleManagementSource;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
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
