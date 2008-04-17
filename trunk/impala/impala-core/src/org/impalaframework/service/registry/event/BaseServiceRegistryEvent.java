package org.impalaframework.service.registry.event;

import org.impalaframework.service.registry.ServiceReference;
import org.springframework.util.Assert;

public abstract class BaseServiceRegistryEvent implements ServiceRegistryEvent {
	private final ServiceReference serviceReference;

	public BaseServiceRegistryEvent(ServiceReference serviceReference) {
		super();
		Assert.notNull(serviceReference);
		this.serviceReference = serviceReference;
	}

	public final ServiceReference getServiceReference() {
		return serviceReference;
	}
	
	
}
