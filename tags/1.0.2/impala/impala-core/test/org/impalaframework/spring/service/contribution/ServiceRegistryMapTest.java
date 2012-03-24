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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.springframework.util.ClassUtils;

public class ServiceRegistryMapTest extends TestCase {

    private ServiceRegistryMap map;
    private ServiceRegistry serviceRegistry;    
    private Class<?>[] supportedTypes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        map = new ServiceRegistryMap();
        serviceRegistry = createMock(ServiceRegistry.class);
        map.setServiceRegistry(serviceRegistry);
        supportedTypes = new Class[]{ List.class };
        map.setProxyTypes(supportedTypes);
    }
    
    public void testNoMapKey() throws Exception {
        LdapServiceReferenceFilter filter = new LdapServiceReferenceFilter("(name=myname)");
        map.setFilter(filter);
        
        ServiceRegistryEntry ref = new StaticServiceRegistryEntry("service", "mybean", "mymodule", ClassUtils.getDefaultClassLoader());
        List<ServiceRegistryEntry> singletonList = Collections.singletonList(ref);
        expect(serviceRegistry.getServices(filter, supportedTypes, false)).andReturn(singletonList);
        expect(serviceRegistry.addEventListener(map)).andReturn(true);
        
        replay(serviceRegistry);
        
        map.afterPropertiesSet();
        assertTrue(map.isEmpty());
        
        verify(serviceRegistry);
    }

    @SuppressWarnings("unchecked")
    public void testWithMapKey() throws Exception {
        List<String> service = new ArrayList<String>();
        
        LdapServiceReferenceFilter filter = new LdapServiceReferenceFilter("(name=myname)");
        map.setFilter(filter);
        
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("mapkey", "key");
        ServiceRegistryEntry ref = new StaticServiceRegistryEntry(service, "mybean", "mymodule", null, attributes, ClassUtils.getDefaultClassLoader());
        List<ServiceRegistryEntry> singletonList = Collections.singletonList(ref);
        expect(serviceRegistry.getServices(filter, supportedTypes, false)).andReturn(singletonList);
        expect(serviceRegistry.addEventListener(map)).andReturn(true);
        
        replay(serviceRegistry);
        
        map.afterPropertiesSet();
        assertFalse(map.isEmpty());
        
        //map service is List but not same instance
        assertTrue(map.get("key") instanceof List);
        assertFalse(map.get("key") == service);
        
        verify(serviceRegistry);
    }
    
    public void testWithNonFinalClass() throws Exception {
        ValueClass service = new ValueClass();
        
        LdapServiceReferenceFilter filter = new LdapServiceReferenceFilter("(name=myname)");
        map.setFilter(filter);
        map.setProxyTypes(null);
        
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("mapkey", "key");
        ServiceRegistryEntry ref = new StaticServiceRegistryEntry(service, "mybean", "mymodule", null, attributes, ClassUtils.getDefaultClassLoader());
        List<ServiceRegistryEntry> singletonList = Collections.singletonList(ref);
        expect(serviceRegistry.getServices(filter, null, false)).andReturn(singletonList);
        expect(serviceRegistry.addEventListener(map)).andReturn(true);
        
        replay(serviceRegistry);
        
        map.afterPropertiesSet();
        assertFalse(map.isEmpty());
        
        //map service is List but not same instance
        Object storedValue = map.get("key");
        assertTrue(storedValue instanceof ServiceRegistryMapTest.ValueClass);
        assertFalse(map.get("key") == service);
        
        ValueClass vc = (ValueClass) storedValue;
        vc.sayHello();
        
        verify(serviceRegistry);
    }
    
    public void testWithFinalClass() throws Exception {
        
        LdapServiceReferenceFilter filter = new LdapServiceReferenceFilter("(name=myname)");
        map.setFilter(filter);
        map.setProxyTypes(null);
        
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("mapkey", "key");
        ServiceRegistryEntry ref = new StaticServiceRegistryEntry("stringservice", "mybean", "mymodule", null, attributes, ClassUtils.getDefaultClassLoader());
        List<ServiceRegistryEntry> singletonList = Collections.singletonList(ref);
        expect(serviceRegistry.getServices(filter, null, false)).andReturn(singletonList);
        
        replay(serviceRegistry);
        
        try {
            map.afterPropertiesSet();
            fail();
        }
        catch (InvalidStateException e) {
            e.printStackTrace();
        }
        
        verify(serviceRegistry);
    }
    
    static class ValueClass {
        public void sayHello() {
            System.out.println("Hello");
        }
    }
    
}
