package org.impalaframework.service.registry.event;

import org.impalaframework.service.registry.ServiceReference;

public class ServiceRemovedEvent extends BaseServiceRegistryEvent {

	public ServiceRemovedEvent(ServiceReference serviceReference) {
		super(serviceReference);
	}

}
