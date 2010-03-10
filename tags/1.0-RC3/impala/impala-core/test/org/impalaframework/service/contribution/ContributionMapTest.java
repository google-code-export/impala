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

package org.impalaframework.service.contribution;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.springframework.util.ClassUtils;

public class ContributionMapTest extends TestCase {

    private ContributionMap map;
    private ServiceRegistryMap serviceRegistryMap;
    private DelegatingServiceRegistry registry;
    private ClassLoader classLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        map = new ContributionMap();
        serviceRegistryMap = new ServiceRegistryMap();
        map.setExternalContributions(serviceRegistryMap);
        serviceRegistryMap.setFilterExpression("(mapkey=*)");
        registry = new DelegatingServiceRegistry();
        serviceRegistryMap.setServiceRegistry(registry);
        classLoader = ClassUtils.getDefaultClassLoader();
    }
    
    public void testEmpty() throws Exception {
        assertFalse(map.containsKey("key"));
        assertFalse(map.containsValue("value"));
        assertNull(map.get("key"));
        assertTrue(map.entrySet().isEmpty());
        assertTrue(map.keySet().isEmpty());
        assertTrue(map.isEmpty());
        assertTrue(map.values().isEmpty());
        assertEquals(0, map.size());
        assertNull(map.remove("key"));
    }
    
    public void testWithValues() throws Exception {
        map.put("key", "value");
        assertTrue(map.containsKey("key"));
        assertTrue(map.containsValue("value"));
        assertNotNull(map.get("key"));
        assertFalse(map.entrySet().isEmpty());
        assertFalse(map.keySet().isEmpty());
        assertFalse(map.isEmpty());
        assertFalse(map.values().isEmpty());
        assertEquals(1, map.size());
        assertNotNull(map.remove("key"));
        
        Map<String,String> m = new HashMap<String, String>();
        m.put("a'", "b");
        map.putAll(m);
        assertEquals(1, map.size());
    }
    
    public void testListener() {

        serviceRegistryMap.init();
        //no need to add explicitly as always present
        assertFalse(registry.addEventListener(serviceRegistryMap));

        String service1 = "some service1";
        String service2 = "some service2";
        
        final ServiceRegistryEntry ref1 = registry.addService("bean1", "module1", new StaticServiceBeanReference(service1), null, Collections.singletonMap("mapkey", "bean1"), classLoader);
        final ServiceRegistryEntry ref2 = registry.addService("bean2", "module1", new StaticServiceBeanReference(service2), null, Collections.singletonMap("mapkey", "bean2"), classLoader);
        assertEquals(2, serviceRegistryMap.size());
        assertNotNull(serviceRegistryMap.get("bean1"));
        assertNotNull(serviceRegistryMap.get("bean2"));
        registry.remove(ref1);
        registry.remove(ref2);      
        assertEquals(0, serviceRegistryMap.size());

        //sucessfully remove listener. Compare with testDestroy where this returns false
        assertTrue(registry.removeEventListener(serviceRegistryMap));
    }
    
    public void testDestroy() throws Exception {
        serviceRegistryMap.init();
        String service1 = "some service1";
        
        final ServiceRegistryEntry entry1 = registry.addService("bean1", "module1", new StaticServiceBeanReference(service1), null, Collections.singletonMap("mapkey", "bean1"), classLoader);
        assertEquals(1, serviceRegistryMap.size());   
        
        serviceRegistryMap.destroy();
        assertTrue(serviceRegistryMap.isEmpty());
        
        //no need to remove listener as this was removed via destroy
        assertFalse(registry.removeEventListener(serviceRegistryMap));

        //service is still present in service registry, though
        assertTrue(registry.remove(entry1));
    }
    
    public void testSuppliedFilter() {

        serviceRegistryMap.setFilter(new LdapServiceReferenceFilter("(mapkey=bean1)"));
        serviceRegistryMap.init();

        //this one will match
        registry.addService("bean1", "module1", new StaticServiceBeanReference("some service1"), null, Collections.singletonMap("mapkey", "bean1"), classLoader);
        
        //this one won't
        registry.addService("bean2", "module1", new StaticServiceBeanReference("some service1"), null, Collections.singletonMap("mapkey", "bean2"), classLoader);
        assertEquals(1, serviceRegistryMap.size());
    }
    
    public void testMapListener() {

        serviceRegistryMap.init();

        String service1 = "value1";
        String service2 = "value2";
        
        registry.addService("bean1", "module1", new StaticServiceBeanReference(service1), null, Collections.singletonMap("mapkey", "bean1"), classLoader);
        registry.addService("bean2", "module1", new StaticServiceBeanReference(service2), null, Collections.singletonMap("mapkey", "bean2"), classLoader);
        assertEquals(2, serviceRegistryMap.size());

        Map<String,String> m = new HashMap<String, String>();
        m.put("bean2", "value2a");
        map.putAll(m);
        map.put("bean3", "value3");
        
        System.out.println(map);
        
        assertTrue(map.containsKey("bean1"));
        assertTrue(map.containsKey("bean2"));
        assertTrue(map.containsKey("bean3"));
        
        assertTrue(map.containsValue("value1"));
        assertTrue(map.containsValue("value2"));
        assertTrue(map.containsValue("value2a"));
        assertTrue(map.containsValue("value3"));
        
        assertNotNull(map.get("bean1"));
        assertNotNull(map.get("bean2"));
        assertNotNull(map.get("bean3"));
        assertFalse(map.entrySet().isEmpty());
        assertFalse(map.keySet().isEmpty());
        assertFalse(map.isEmpty());
        assertFalse(map.values().isEmpty());
        assertEquals(4, map.size());
        assertNotNull(map.remove("bean3"));
        assertEquals(3, map.size());
    }
    
    public void testGetExistingServices() throws Exception {
        
        String service1 = "value1";
        String service2 = "value2";
        
        registry.addService("bean1", "module1", new StaticServiceBeanReference(service1), null, Collections.singletonMap("mapkey", "bean1"), classLoader);
        registry.addService("bean2", "module1", new StaticServiceBeanReference(service2), null, Collections.singletonMap("mapkey", "bean2"), classLoader);
        
        serviceRegistryMap.init();
        assertEquals(2, serviceRegistryMap.size());
    }
    
}
