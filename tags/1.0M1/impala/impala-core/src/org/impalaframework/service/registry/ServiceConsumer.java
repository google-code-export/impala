package org.impalaframework.service.registry;

import org.impalaframework.service.registry.event.ServiceReferenceFilter;
import org.impalaframework.service.registry.event.ServiceRegistryEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEventListener;

// FIXME implement this		
public class ServiceConsumer implements ServiceRegistryEventListener {

	private ServiceReferenceFilter filter;
	
	public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
	}

	public void setFilter(ServiceReferenceFilter filter) {
		this.filter = filter;
	}

}
