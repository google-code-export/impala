package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;
import org.springframework.util.Assert;

public class ReloadNewNamedPluginOperation extends ReloadNamedPluginOperation {

	private ModuleDefinitionSource pluginSpecProvider;

	public ReloadNewNamedPluginOperation(ModuleManagementSource factory, String pluginName,
			ModuleDefinitionSource pluginSpecProvider) {
		super(factory, pluginName);
		Assert.notNull(pluginSpecProvider);
		this.pluginSpecProvider = pluginSpecProvider;
	}

	@Override
	protected RootModuleDefinition newPluginSpec() {
		return pluginSpecProvider.getPluginSpec();
	}

}
