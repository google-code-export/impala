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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.springframework.util.ClassUtils;

public class BaseServiceRegistryListTest extends TestCase {
    
    private BaseServiceRegistryList list;

    @Override
    protected void setUp() throws Exception {
        super.setUp();        
        list = new BaseServiceRegistryList() {

            @Override
            protected Object maybeGetProxy(ServiceRegistryEntry entry) {
                return entry.getServiceBeanReference().getService();
            }
        };
    }

    public void testAddRemove() throws Exception {

        BasicServiceRegistryEntry ref1 = new StaticServiceRegistryEntry("service1", "beanName1", "module", null, Collections.singletonMap("service.ranking", 0), ClassUtils.getDefaultClassLoader());
        BasicServiceRegistryEntry ref2 = new StaticServiceRegistryEntry("service2", "beanName2", "module", null, Collections.singletonMap("service.ranking", 100), ClassUtils.getDefaultClassLoader());
        list.add(ref1);
        assertTrue(list.add(ref2));
        
        //service2 has higher service ranking
        assertEquals("service2", list.get(0));
        
        //second time you add it in, no effect
        assertFalse(list.add(ref2));
        
        assertEquals(2, list.size());
        
        assertTrue(list.remove(ref2));
        assertEquals(1, list.size());
        
        //second removal has not effect
        assertFalse(list.remove(ref2));

        assertTrue(list.remove(ref1));
        assertTrue(list.isEmpty());
    }
    
    public void testWithListener() throws Exception {
        DelegatingServiceRegistry registry = new DelegatingServiceRegistry();
        list.setServiceRegistry(registry);
        list.setFilterExpression("(mapkey=*)");

        List<String> service1 = new ArrayList<String>();
        List<String> service2 = new ArrayList<String>();
        List<String> service3 = new ArrayList<String>();
        
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        registry.addService("bean1", "module1", new StaticServiceBeanReference(service1), null, Collections.singletonMap("mapkey", "bean1"), classLoader);
        registry.addService("bean2", "module1", new StaticServiceBeanReference(service2), null, Collections.singletonMap("mapkey", "bean2"), classLoader);
        
        assertTrue(list.isEmpty());
        
        //now call init to add
        list.init();
        
        assertEquals(2, list.size());
        
        //now add service and see it automatically reflect
        registry.addService("bean3", "module1", new StaticServiceBeanReference(service3), null, Collections.singletonMap("mapkey", "bean3"), classLoader);
        assertEquals(3, list.size());
        
        list.destroy();
        assertTrue(list.isEmpty());        
        
        //no need to remove listener as this was removed via destroy
        assertFalse(registry.removeEventListener(list));
    }
    
}
