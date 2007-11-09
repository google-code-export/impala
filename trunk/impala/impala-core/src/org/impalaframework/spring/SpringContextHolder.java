package org.impalaframework.spring;

import java.util.Map;

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public interface SpringContextHolder {

	void shutParentContext();

	boolean loadParentContext(ParentSpec spec);

	void setSpringContextSpec(ParentSpec spec);

	boolean loadParentContext();

	boolean addPlugin(PluginSpec plugin);

	void closePlugin(PluginSpec remove);

	ApplicationContext getContext();

	ParentSpec getParent();

	PluginSpec getPlugin(String pluginName);

	PluginSpec findPluginLike(String pluginLikeName);

	boolean hasPlugin(String pluginName);

	ApplicationContextLoader getContextLoader();

	Map<String, ConfigurableApplicationContext> getPlugins();

	boolean hasParentContext();

}