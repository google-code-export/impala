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
public class StaticServiceRegistryTargetSource extends BaseServiceRegistryTargetSource {

	private final ServiceRegistryReference reference;
	
	public StaticServiceRegistryTargetSource(ServiceRegistryReference reference) {
		super();
		Assert.notNull(reference, "reference cannot be null");
		this.reference = reference;
	}

	public Object getTarget() throws Exception {
		Object bean = reference.getBean();
		if (bean instanceof FactoryBean) {
			FactoryBean fb = (FactoryBean) bean;
			return fb.getObject();
		}
		return bean;
	}

	public boolean isStatic() {
		//FIXME should this be false
		return true;
	}

	public ServiceRegistryReference getServiceRegistryReference() {
		return reference;
	}

}
