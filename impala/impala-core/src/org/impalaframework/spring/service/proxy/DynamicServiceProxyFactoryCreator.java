package org.impalaframework.spring.service.proxy;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.impalaframework.spring.service.registry.ServiceRegistryTargetSource;

/**
 * Implementation of {@link ServiceProxyFactoryCreator} which is used to create proxy for a bean which holds a dynamic reference
 * to a bean obtained from the Impala service registry.
 * 
 * @author Phil Zoio
 */
public class DynamicServiceProxyFactoryCreator extends BaseServiceProxyFactoryCreator {
	
	protected ContributionEndpointTargetSource newTargetSource(ServiceRegistry serviceRegistry, String registryKeyName) {
		return new ServiceRegistryTargetSource(registryKeyName, serviceRegistry);
	}
	
}
