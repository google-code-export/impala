package org.impalaframework.module.definition;


public class ConstructedModuleDefinitionSource implements ModuleDefinitionSource {

	private RootModuleDefinition definition;
	
	public ConstructedModuleDefinitionSource(RootModuleDefinition definition) {
		super();
		this.definition = definition;
	}

	public RootModuleDefinition getModuleDefinition() {
		return definition;
	}

}
