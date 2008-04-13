package org.impalaframework.service.registry;

public interface ServiceRegistry {

	void addService(String beanName, String moduleName, Object service);

	void remove(String beanName, String moduleName, Object service);

	ServiceReference getService(String beanName);

}