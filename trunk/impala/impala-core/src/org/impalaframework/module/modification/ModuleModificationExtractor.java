package org.impalaframework.module.modification;

import org.impalaframework.module.definition.RootModuleDefinition;

public interface ModuleModificationExtractor {

	ModuleTransitionSet getTransitions(RootModuleDefinition originalSpec, RootModuleDefinition newSpec);

	//FIXME shouldn't need this method
	ModuleTransitionSet reload(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, String pluginToReload);

}