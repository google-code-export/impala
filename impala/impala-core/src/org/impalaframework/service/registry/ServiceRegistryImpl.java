package org.impalaframework.service.registry;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistryImpl implements ServiceRegistry {

	private Map<String, ServiceReference> services = new ConcurrentHashMap<String, ServiceReference>();
	private Map<Object, String> entities = new IdentityHashMap<Object, String>();

	private Object lock = new Object();

	public void addService(String beanName, String moduleName, Object service) {
		synchronized (lock) {
			services.put(beanName, new ServiceReference(service, moduleName));
			entities.put(service, beanName);
		}
	}
	
	public void remove(Object service) {
		synchronized (lock) {
			String beanName = entities.remove(service);
			if (beanName != null) {
				 services.remove(beanName);
			}
		}
	}

	public ServiceReference getService(String beanName) {
		return services.get(beanName);
	}

}
