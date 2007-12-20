package org.impalaframework.module.holder;

import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.TransitionSet;
import org.springframework.context.ConfigurableApplicationContext;

public interface ModuleStateHolder extends ModuleDefinitionSource {

	void processTransitions(TransitionSet pluginTransitions);

	ConfigurableApplicationContext getParentContext();

	ConfigurableApplicationContext getModule(String name);

	RootModuleDefinition getRootModuleDefinition();

	RootModuleDefinition cloneParentSpec();

	boolean hasPlugin(String plugin);

	boolean hasParentContext();

	Map<String, ConfigurableApplicationContext> getModuleContexts();

	void putPlugin(String name, ConfigurableApplicationContext context);

	ConfigurableApplicationContext removePlugin(String name);

}