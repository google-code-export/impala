package org.impalaframework.service.registry;

public interface ServiceRegistry {

	void addService(String beanName, String moduleName, Object service);
	
	void remove(Object service);

	ServiceReference getService(String beanName);

}