package org.impalaframework.module.definition;


public class ConstructedModuleDefinitionSource implements ModuleDefinitionSource {

	private RootModuleDefinition pluginSpec;
	
	public ConstructedModuleDefinitionSource(RootModuleDefinition pluginSpec) {
		super();
		this.pluginSpec = pluginSpec;
	}

	public RootModuleDefinition getModuleDefintion() {
		return pluginSpec;
	}

}
