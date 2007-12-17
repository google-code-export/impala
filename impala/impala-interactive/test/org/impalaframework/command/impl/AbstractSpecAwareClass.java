package org.impalaframework.command.impl;

import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;


public abstract class AbstractSpecAwareClass implements ModuleDefinitionSource {

	public RootModuleDefinition getModuleDefintion() {
		return null;
	}

}
