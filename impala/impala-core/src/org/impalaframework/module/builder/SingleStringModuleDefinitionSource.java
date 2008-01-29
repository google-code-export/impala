package org.impalaframework.module.builder;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;

public class SingleStringModuleDefinitionSource implements ModuleDefinitionSource {

	private SingleStringSourceDelegate delegate;

	public SingleStringModuleDefinitionSource(RootModuleDefinition rootModuleDefinition, String definitionString) {
		super();
		this.delegate = new SingleStringSourceDelegate(rootModuleDefinition, definitionString);
	}

	public RootModuleDefinition getModuleDefinition() {
		return (RootModuleDefinition) this.delegate.getModuleDefinition();
	}

}
