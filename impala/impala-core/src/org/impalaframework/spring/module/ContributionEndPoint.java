package org.impalaframework.spring.module;

public interface ContributionEndPoint {

	void registerTarget(String pluginName, Object bean);

	void deregisterTarget(Object bean);

}