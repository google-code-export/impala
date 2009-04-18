package org.impalaframework.service.registry;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.impalaframework.spring.service.registry.DynamicServiceRegistryTargetSource;
import org.springframework.util.ClassUtils;

public class DynamicServiceRegistryTargetSourceTest extends TestCase {

    private ServiceRegistryImpl serviceRegistry;

    public void testGetTarget() {
        serviceRegistry = new ServiceRegistryImpl();
        DynamicServiceRegistryTargetSource targetSource = new DynamicServiceRegistryTargetSource("mybean", new Class[]{ Object.class }, serviceRegistry);
        
        assertNull(targetSource.getServiceRegistryReference());
        
        Object service = new Object();
        final ServiceRegistryReference serviceReference = serviceRegistry.addService("mybean", "moduleName", service, ClassUtils.getDefaultClassLoader());
        
        assertNotNull(targetSource.getServiceRegistryReference());
        
        serviceRegistry.remove(serviceReference);
        
        assertNull(targetSource.getServiceRegistryReference());
    }

}
