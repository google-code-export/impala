package org.impalaframework.service.registry.event;

import org.impalaframework.service.registry.ServiceReference;

public interface ServiceReferenceFilter {
	public boolean matches(ServiceReference reference);
}
