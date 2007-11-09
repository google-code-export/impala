package org.impalaframework.spring.plugin;

public interface PluginContributionEndPoint {

	//FIXME expose interface
	//FIXME add plugin name as parameter
	void registerTarget(String pluginName, Object bean);

	void deregisterTarget(Object bean);

}