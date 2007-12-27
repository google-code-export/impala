package org.impalaframework.module.modification;

import org.impalaframework.module.definition.RootModuleDefinition;

public interface ModificationExtractor {

	TransitionSet getTransitions(RootModuleDefinition originalSpec, RootModuleDefinition newSpec);

}