package org.impalaframework.spring.plugin;

public interface ContributionEndPoint {

	void registerTarget(String pluginName, Object bean);

	void deregisterTarget(Object bean);

}