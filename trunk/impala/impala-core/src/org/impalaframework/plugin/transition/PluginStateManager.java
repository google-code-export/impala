package org.impalaframework.plugin.transition;

import java.util.Map;

import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.springframework.context.ConfigurableApplicationContext;

public interface PluginStateManager {

	void processTransitions(PluginTransitionSet pluginTransitions);

	ConfigurableApplicationContext getParentContext();

	ConfigurableApplicationContext getPlugin(String name);

	ParentSpec getParentSpec();

	ParentSpec cloneParentSpec();

	boolean hasPlugin(String plugin);

	boolean hasParentContext();

	Map<String, ConfigurableApplicationContext> getPlugins();

	void putPlugin(String name, ConfigurableApplicationContext context);

	ConfigurableApplicationContext removePlugin(String name);

}