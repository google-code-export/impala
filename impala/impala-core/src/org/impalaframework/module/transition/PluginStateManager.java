package org.impalaframework.module.transition;

import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.springframework.context.ConfigurableApplicationContext;

public interface PluginStateManager extends ModuleDefinitionSource {

	void processTransitions(ModuleTransitionSet pluginTransitions);

	ConfigurableApplicationContext getParentContext();

	ConfigurableApplicationContext getPlugin(String name);

	RootModuleDefinition getParentSpec();

	RootModuleDefinition cloneParentSpec();

	boolean hasPlugin(String plugin);

	boolean hasParentContext();

	Map<String, ConfigurableApplicationContext> getPlugins();

	void putPlugin(String name, ConfigurableApplicationContext context);

	ConfigurableApplicationContext removePlugin(String name);

}