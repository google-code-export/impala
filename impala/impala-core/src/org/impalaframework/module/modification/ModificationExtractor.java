package org.impalaframework.module.modification;

import org.impalaframework.module.definition.RootModuleDefinition;

public interface ModificationExtractor {

	TransitionSet getTransitions(RootModuleDefinition originalSpec, RootModuleDefinition newSpec);

	//FIXME shouldn't need this method
	TransitionSet reload(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, String pluginToReload);

}