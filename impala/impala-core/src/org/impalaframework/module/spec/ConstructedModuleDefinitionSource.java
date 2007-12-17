package org.impalaframework.module.spec;


public class ConstructedModuleDefinitionSource implements ModuleDefinitionSource {

	private RootModuleDefinition pluginSpec;
	
	public ConstructedModuleDefinitionSource(RootModuleDefinition pluginSpec) {
		super();
		this.pluginSpec = pluginSpec;
	}

	public RootModuleDefinition getPluginSpec() {
		return pluginSpec;
	}

}
