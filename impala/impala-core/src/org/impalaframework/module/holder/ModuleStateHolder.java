package org.impalaframework.module.holder;

import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.TransitionSet;
import org.springframework.context.ConfigurableApplicationContext;

public interface ModuleStateHolder extends ModuleDefinitionSource {

	void processTransitions(TransitionSet transitions);

	ConfigurableApplicationContext getParentContext();

	ConfigurableApplicationContext getModule(String name);

	RootModuleDefinition getRootModuleDefinition();

	RootModuleDefinition cloneRootModuleDefinition();

	boolean hasModule(String name);

	boolean hasParentContext();

	Map<String, ConfigurableApplicationContext> getModuleContexts();

	void putModule(String name, ConfigurableApplicationContext context);

	ConfigurableApplicationContext removeModule(String name);

}