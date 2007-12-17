package org.impalaframework.module.modification;

import org.impalaframework.module.spec.RootModuleDefinition;

public interface PluginModificationCalculator {

	PluginTransitionSet getTransitions(RootModuleDefinition originalSpec, RootModuleDefinition newSpec);

	//FIXME shouldn't need this method
	PluginTransitionSet reload(RootModuleDefinition originalSpec, RootModuleDefinition newSpec, String pluginToReload);

}