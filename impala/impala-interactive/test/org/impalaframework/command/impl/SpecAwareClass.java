package org.impalaframework.command.impl;

import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;


public class SpecAwareClass implements ModuleDefinitionSource {

	public RootModuleDefinition getPluginSpec() {
		return null;
	}

}
