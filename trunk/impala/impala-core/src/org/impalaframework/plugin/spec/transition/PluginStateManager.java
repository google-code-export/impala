package org.impalaframework.plugin.spec.transition;

import java.util.Map;

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.modification.PluginTransitionSet;
import org.springframework.context.ConfigurableApplicationContext;

public interface PluginStateManager {

	void processTransitions(PluginTransitionSet pluginTransitions);

	ConfigurableApplicationContext getParentContext();

	ConfigurableApplicationContext getPlugin(String name);

	ApplicationContextLoader getContextLoader();

	ParentSpec getParentSpec();

	ParentSpec cloneParentSpec();

	void setParentSpec(ParentSpec parentSpec);

	boolean hasPlugin(String plugin);

	boolean hasParentContext();

	Map<String, ConfigurableApplicationContext> getPlugins();

	void putPlugin(String name, ConfigurableApplicationContext context);

	ConfigurableApplicationContext removePlugin(String name);

	void setApplicationContextLoader(ApplicationContextLoader contextLoader);

}