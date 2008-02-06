package org.impalaframework.spring.module;

public interface ContributionEndpoint {

	void registerTarget(String moduleName, Object bean);

	void deregisterTarget(Object bean);

}