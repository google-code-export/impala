package org.impalaframework.plugin.modification;

import org.impalaframework.plugin.spec.ParentSpec;

public interface PluginModificationCalculator {

	PluginTransitionSet getTransitions(ParentSpec originalSpec, ParentSpec newSpec);

	//FIXME shouldn't need this method
	PluginTransitionSet reload(ParentSpec originalSpec, ParentSpec newSpec, String pluginToReload);

}