package org.impalaframework.spring.service.proxy;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;

/**
 * @author Phil Zoio
 */
public class StaticServiceProxyFactoryCreator extends BaseServiceProxyFactoryCreator {

	protected ContributionEndpointTargetSource newTargetSource(ServiceRegistry serviceRegistry, String registryKeyName) {
		return null;//new StaticServiceRegistryTargetSource(registryKeyName, serviceRegistry);
	}
}
