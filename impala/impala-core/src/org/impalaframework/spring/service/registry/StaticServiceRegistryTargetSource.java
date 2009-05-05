package org.impalaframework.spring.service.registry;

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ContributionEndpointTargetSource} which is designed to hold a static 
 * reference to a {@link ServiceRegistryEntry} instance.
 * 
 * @author Phil Zoio
 */
public class StaticServiceRegistryTargetSource extends BaseServiceRegistryTargetSource {

    private final ServiceRegistryEntry reference;
    
    public StaticServiceRegistryTargetSource(ServiceRegistryEntry reference) {
        super();
        Assert.notNull(reference, "reference cannot be null");
        this.reference = reference;
    }

    public Object getTarget() throws Exception {
        Object bean = reference.getService().getService();
        if (bean instanceof FactoryBean) {
            FactoryBean fb = (FactoryBean) bean;
            return fb.getObject();
        }
        return bean;
    }
    
    @Override
    public Class<?> getTargetClass() {
        return reference.getService().getService().getClass();
    }

    public boolean isStatic() {
        //FIXME should this be false
        return true;
    }

    public ServiceRegistryEntry getServiceRegistryReference() {
        return reference;
    }

}
