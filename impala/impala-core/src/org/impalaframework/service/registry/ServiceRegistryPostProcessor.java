package org.impalaframework.service.registry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

public class ServiceRegistryPostProcessor implements BeanPostProcessor {
	
	private final ServiceRegistry serviceRegistryAware;

	public ServiceRegistryPostProcessor(ServiceRegistry moduleDefinition) {
		Assert.notNull(moduleDefinition);
		this.serviceRegistryAware = moduleDefinition;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof ServiceRegistryAware) {
			ServiceRegistryAware psa = (ServiceRegistryAware) bean;
			psa.setServiceRegistry(serviceRegistryAware);
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String name) {
		return bean;
	}
}
