/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.spring.service.contribution;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.springframework.util.ClassUtils;

public class ServiceRegistrySetTest extends TestCase {

    private ServiceRegistrySet set;
    private ServiceRegistry serviceRegistry;
    private Class<?>[] supportedTypes;
    private LdapServiceReferenceFilter filter;
    private ClassLoader classLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        set = new ServiceRegistrySet();
        
        serviceRegistry = new DelegatingServiceRegistry();
        set.setServiceRegistry(serviceRegistry);
        supportedTypes = new Class[]{ List.class };
        set.setProxyTypes(supportedTypes);
        
        filter = new LdapServiceReferenceFilter("(name=*)");
        set.setFilter(filter);
        
        classLoader = ClassUtils.getDefaultClassLoader();
    }

    @SuppressWarnings("unchecked")
    public void testWithList() throws Exception {

        serviceRegistry = createMock(ServiceRegistry.class);
        set.setServiceRegistry(serviceRegistry);
        List<String> service = new ArrayList<String>();
        ServiceRegistryEntry ref = new StaticServiceRegistryEntry(service, "mybean", "mymodule", ClassUtils.getDefaultClassLoader());
        List<ServiceRegistryEntry> singletonList = Collections.singletonList(ref);
        expect(serviceRegistry.getServices(filter, supportedTypes, false)).andReturn(singletonList);
        expect(serviceRegistry.addEventListener(set)).andReturn(true);
        
        replay(serviceRegistry);
        
        set.afterPropertiesSet();
        assertFalse(set.isEmpty());
        
        assertTrue(set.iterator().next() instanceof List);
        assertFalse(set.iterator().next() instanceof ArrayList);
        
        verify(serviceRegistry);
        
        set.remove(ref);
        assertTrue(set.isEmpty());
    }
    
    public void testWithNoExportTypes() throws Exception {
        List<String> service = new ArrayList<String>();
        serviceRegistry.addService("mybean", "mymodule", new StaticServiceBeanReference(service), null, Collections.singletonMap("name", "value"), classLoader);
        set.afterPropertiesSet();
        
        assertFalse(set.isEmpty());
    }
    
    public void testWithExportTypes() throws Exception {
        set.setExportTypes(supportedTypes);
        
        List<String> service = new ArrayList<String>();
        serviceRegistry.addService("mybean", "mymodule", new StaticServiceBeanReference(service), null, Collections.singletonMap("name", "value"), classLoader);
        set.afterPropertiesSet();
        
        assertTrue(set.isEmpty());
            
        //add another service which does not specify export types
        serviceRegistry.addService("mybean", "mymodule", new StaticServiceBeanReference(service), null, Collections.singletonMap("name", "value"), classLoader);
        assertTrue(set.isEmpty());

        //add another service which does specify export types
        serviceRegistry.addService("mybean", "mymodule", new StaticServiceBeanReference(service), Arrays.asList(supportedTypes), Collections.singletonMap("name", "value"), classLoader);
        assertFalse(set.isEmpty());   
    }
    
    public void testWithExportTypesBeforeInit() throws Exception {
        set.setExportTypes(supportedTypes);
        
        List<String> service = new ArrayList<String>();
        serviceRegistry.addService("mybean", "mymodule", new StaticServiceBeanReference(service), Arrays.asList(supportedTypes), Collections.singletonMap("name", "value"), classLoader);
        set.afterPropertiesSet();
        
        assertFalse(set.isEmpty());
    }
    
    static class ValueClass {
        public void sayHello() {
            System.out.println("Hello");
        }
    }
    
}
