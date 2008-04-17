package org.impalaframework.service.registry.event;

public interface ServiceRegistryEventListener {
	void handleServiceRegistryEvent(ServiceRegistryEvent event);
}
