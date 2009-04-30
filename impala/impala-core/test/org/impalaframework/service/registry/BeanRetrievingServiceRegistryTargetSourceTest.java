package org.impalaframework.service.registry;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.impalaframework.spring.service.registry.BeanRetrievingServiceRegistryTargetSource;
import org.springframework.util.ClassUtils;

public class BeanRetrievingServiceRegistryTargetSourceTest extends TestCase {

    private ServiceRegistryImpl serviceRegistry;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry = new ServiceRegistryImpl();
    }

    public void testGetUsingBean() {
        BeanRetrievingServiceRegistryTargetSource targetSource = new BeanRetrievingServiceRegistryTargetSource(serviceRegistry, "mybean", new Class[]{ Object.class }, false);
        
        assertNull(targetSource.getServiceRegistryReference());
        
        Object service = new Object();
        final ServiceRegistryReference serviceReference = serviceRegistry.addService("mybean", "moduleName", service, ClassUtils.getDefaultClassLoader());
        
        assertNotNull(targetSource.getServiceRegistryReference());
        
        serviceRegistry.remove(serviceReference);
        
        assertNull(targetSource.getServiceRegistryReference());
    }
    
    public void testGetUsingExportedType() {
        BeanRetrievingServiceRegistryTargetSource targetSource = new BeanRetrievingServiceRegistryTargetSource(serviceRegistry, null, new Class[]{ Object.class }, true);
        
        assertNull(targetSource.getServiceRegistryReference());
        
        Object service = new Object();
        List<Class<?>> asList = Arrays.asList(new Class<?>[]{Object.class});
        final ServiceRegistryReference serviceReference = serviceRegistry.addService("mybean", "moduleName", service, asList, null, ClassUtils.getDefaultClassLoader());
        
        assertNotNull(targetSource.getServiceRegistryReference());
        
        serviceRegistry.remove(serviceReference);
        
        assertNull(targetSource.getServiceRegistryReference());
    }

}
