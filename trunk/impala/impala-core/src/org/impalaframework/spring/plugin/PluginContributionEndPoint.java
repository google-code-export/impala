package org.impalaframework.spring.plugin;

public interface PluginContributionEndPoint {

	void registerTarget(String pluginName, Object bean);

	void deregisterTarget(Object bean);

}