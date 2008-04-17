package org.impalaframework.service.registry;

import java.util.List;
import java.util.Map;

public interface ServiceRegistry {

	void addService(String beanName, String moduleName, Object service);	

    void addService(String beanName, String moduleName, Object service, List<String> tags);
	
	void addService(String beanName, String moduleName, Object service, Map<String,?> attributes);

	void addService(String beanName, String moduleName, Object service, List<String> tags, Map<String,?> attributes);

	void remove(Object service);

	ServiceReference getService(String beanName);
	
	ServiceReference getService(String beanName, Class<?> type);

}