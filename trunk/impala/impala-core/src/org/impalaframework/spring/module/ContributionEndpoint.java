package org.impalaframework.spring.module;

public interface ContributionEndpoint {

	void registerTarget(String pluginName, Object bean);

	void deregisterTarget(Object bean);

}