package org.impalaframework.service.registry.event;

import org.impalaframework.service.registry.ServiceReference;

public interface ServiceRegistryEvent {
	public ServiceReference getServiceReference();
}
