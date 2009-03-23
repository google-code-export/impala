package org.impalaframework.spring.service.registry;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ContributionEndpointTargetSource} which is designed to hold a static 
 * reference to a {@link ServiceRegistryReference} instance.
 * 
 * @author Phil Zoio
 */
public class StaticServiceRegistryTargetSource implements ContributionEndpointTargetSource {

	private final ServiceRegistryReference reference;
	
	public StaticServiceRegistryTargetSource(ServiceRegistryReference reference) {
		super();
		Assert.notNull(reference, "reference cannot be null");
		this.reference = reference;
	}

	public ServiceRegistryReference getServiceRegistryReference() {
		return reference;
	}

	public boolean hasTarget() {
		return true;
	}

	public Object getTarget() throws Exception {
		Object bean = reference.getBean();
		if (bean instanceof FactoryBean) {
			FactoryBean fb = (FactoryBean) bean;
			return fb.getObject();
		}
		return bean;
	}

	public Class<?> getTargetClass() {
		return null;
	}

	public boolean isStatic() {
		return true;
	}

	public void releaseTarget(Object target) throws Exception {
	}

}
