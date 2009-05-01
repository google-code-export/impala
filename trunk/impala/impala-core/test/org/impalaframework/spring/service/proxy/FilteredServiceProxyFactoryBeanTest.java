/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.spring.service.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.springframework.util.ClassUtils;

public class FilteredServiceProxyFactoryBeanTest extends TestCase {
    
    private ServiceRegistryImpl serviceRegistry;
    private FilteredServiceProxyFactoryBean factoryBean;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceRegistry = new ServiceRegistryImpl();
        factoryBean = new FilteredServiceProxyFactoryBean();
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
    public void testClassProxy() throws Exception {

        factoryBean.setProxyTypes(new Class[]{ArrayList.class});
        ArrayList<String> service = new ArrayList<String>();
        serviceRegistry.addService("beanName", "moduleName", service, null, Collections.singletonMap("name", "value"), ClassUtils.getDefaultClassLoader());
        
        factoryBean.afterPropertiesSet();
        Object object = factoryBean.getObject();
        assertNotNull(object);
        
        assertTrue(object instanceof ArrayList);
        
        //check that this is actually a proxy, not the class itself
        assertFalse(object.getClass().getName().equals(ArrayList.class.getName()));
        List<String> list = (List<String>) object;
        
        //check that we can use the proxy
        list.add("some string");
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
