package org.impalaframework.spring.service.proxy;

import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Encapsulates the mechanism for creating a {@link ProxyFactory} which backs a service/bean
 * obtained from the Impala service registry.
 * 
 * @author Phil Zoio
 */
public interface ServiceProxyFactoryCreator extends ServiceRegistryAware {

	ProxyFactory createProxy(Class<?>[] interfaces, String registryKeyName);

}