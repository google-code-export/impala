package org.impalaframework.spring.service.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.springframework.util.ClassUtils;

public class ServiceProxyFactoryBeanTest extends TestCase {
    
    private ServiceRegistryImpl serviceRegistry;
    private ServiceProxyFactoryBean factoryBean;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry = new ServiceRegistryImpl();
        factoryBean = new ServiceProxyFactoryBean();
        factoryBean.setServiceRegistry(serviceRegistry);
        factoryBean.setFilterExpression("(name=*)");
        factoryBean.setProxyTypes(new Class[]{List.class});
    }

    @SuppressWarnings("unchecked")
    public void testCreateDestroy() throws Exception {
        
        ArrayList<String> service = new ArrayList<String>();
        serviceRegistry.addService("beanName", "moduleName", service, null, Collections.singletonMap("name", "value"), ClassUtils.getDefaultClassLoader());
        
        factoryBean.afterPropertiesSet();
        Object object = factoryBean.getObject();
        assertNotNull(object);
        
        assertTrue(object instanceof List);
        List<String> list = (List<String>) object;
        list.add("some string");
        
        assertFalse(object instanceof ArrayList);
        assertTrue(service.size() > 0);        

        //listener already present so not registered
        assertFalse(serviceRegistry.addEventListener(factoryBean.getList()));  
        
        factoryBean.destroy();
        //listener already removed, so no need to remove it
        assertFalse(serviceRegistry.removeEventListener(factoryBean.getList()));        
                
        noService(list);
    }

    @SuppressWarnings("unchecked")
    public void testRemoveService() throws Exception {
        
        ArrayList<String> service = new ArrayList<String>();
        
        factoryBean.afterPropertiesSet();
        Object object = factoryBean.getObject();
        assertNotNull(object);
        List<String> list = (List<String>) object;
        
        //no service
        noService(list);

        //add service and see we can call
        ServiceRegistryReference reference = serviceRegistry.addService("beanName", "moduleName", service, null, Collections.singletonMap("name", "value"), ClassUtils.getDefaultClassLoader());
       
        list.add("some string");
        list.add("some string");
        
        //remove registry
        serviceRegistry.remove(reference);
        
        noService(list);
    }

    private void noService(List<String> list) {
        try {
            list.add("another string");
            fail();
        }
        catch (NoServiceException e) {
        }
    }
    
    
}
