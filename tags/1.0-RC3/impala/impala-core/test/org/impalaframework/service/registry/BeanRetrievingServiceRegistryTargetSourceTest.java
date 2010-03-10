package org.impalaframework.service.registry;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.impalaframework.spring.service.registry.BeanRetrievingServiceRegistryTargetSource;
import org.springframework.util.ClassUtils;

public class BeanRetrievingServiceRegistryTargetSourceTest extends TestCase {

    private DelegatingServiceRegistry serviceRegistry;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry = new DelegatingServiceRegistry();
    }

    public void testGetUsingBean() {
        BeanRetrievingServiceRegistryTargetSource targetSource = new BeanRetrievingServiceRegistryTargetSource(serviceRegistry, 
                "mybean", //bean name null
                new Class[]{ Object.class }, //proxy types non null
                null //export types null
                );
        
        assertNull(targetSource.getServiceRegistryReference());
        
        Object service = new Object();
        final ServiceRegistryEntry serviceReference = serviceRegistry.addService("mybean", "moduleName", new StaticServiceBeanReference(service), ClassUtils.getDefaultClassLoader());
        
        assertNotNull(targetSource.getServiceRegistryReference());
        
        serviceRegistry.remove(serviceReference);
        
        assertNull(targetSource.getServiceRegistryReference());
    }
    
    public void testGetUsingExportedType() {
        BeanRetrievingServiceRegistryTargetSource targetSource = new BeanRetrievingServiceRegistryTargetSource(serviceRegistry, 
                null, //bean name null
                null, //proxy types null
                new Class[]{ Object.class } //export types non null
        );
        
        assertNull(targetSource.getServiceRegistryReference());
        
        Object service = new Object();
        List<Class<?>> asList = Arrays.asList(new Class<?>[]{Object.class});
        final ServiceRegistryEntry serviceReference = serviceRegistry.addService("mybean", "moduleName", new StaticServiceBeanReference(service), asList, null, ClassUtils.getDefaultClassLoader());
        
        assertNotNull(targetSource.getServiceRegistryReference());
        
        serviceRegistry.remove(serviceReference);
        
        assertNull(targetSource.getServiceRegistryReference());
    }
    
    public void testNoNameOrExportType() throws Exception {
        try {
            new BeanRetrievingServiceRegistryTargetSource(serviceRegistry, 
                    null, //bean name null
                    new Class[]{ Object.class }, //proxy types non null
                    null //export types null
            );
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("exportTypes and bean name cannot both be null/empty", e.getMessage());
        }
    }
    
    public void testNoProxyOrExportType() throws Exception {
        try {
            new BeanRetrievingServiceRegistryTargetSource(serviceRegistry, 
                    "name", //bean name null
                    null, //proxy types null
                    null //export types non null
            );
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("exportTypes and proxyTypes cannot both be null/empty", e.getMessage());
        }
    }


}
