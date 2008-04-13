package org.impalaframework.service.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistryImpl implements ServiceRegistry {

	private Map<String, ServiceReference> services = new ConcurrentHashMap<String, ServiceReference>();

	public void addService(String beanName, String moduleName, Object service) {
		services.put(beanName, new ServiceReference(service, moduleName));
	}
	
	public void remove(String beanName, String moduleName, Object service) {
		services.remove(beanName);
	}
	
	public ServiceReference getService(String beanName) {
		return services.get(beanName);
	}	
	
}
