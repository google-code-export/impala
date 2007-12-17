package org.impalaframework.module.transition;

import java.util.Map;

import org.impalaframework.module.modification.PluginTransitionSet;
import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpecProvider;
import org.springframework.context.ConfigurableApplicationContext;

public interface PluginStateManager extends PluginSpecProvider {

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