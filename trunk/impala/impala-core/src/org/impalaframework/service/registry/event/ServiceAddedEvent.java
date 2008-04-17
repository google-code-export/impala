package org.impalaframework.service.registry.event;

import org.impalaframework.service.registry.ServiceReference;

public class ServiceAddedEvent extends BaseServiceRegistryEvent {

	public ServiceAddedEvent(ServiceReference serviceReference) {
		super(serviceReference);
	}

}
