package org.impalaframework.command.basic;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;


public abstract class AbstractSpecAwareClass implements ModuleDefinitionSource {

	public RootModuleDefinition getModuleDefinition() {
		return null;
	}

}
