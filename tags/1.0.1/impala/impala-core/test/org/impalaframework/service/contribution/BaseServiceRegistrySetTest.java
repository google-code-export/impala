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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.springframework.util.ClassUtils;

public class BaseServiceRegistrySetTest extends TestCase {
    
    private BaseServiceRegistrySet set;

    @Override
    protected void setUp() throws Exception {
        super.setUp();        
        set = new BaseServiceRegistrySet() {

            @Override
            protected Object maybeGetProxy(ServiceRegistryEntry entry) {
                return entry.getServiceBeanReference().getService();
            }
        };
    }

    public void testAddRemove() throws Exception {

        BasicServiceRegistryEntry ref1 = new StaticServiceRegistryEntry("service1", "beanName1", "module", null, Collections.singletonMap("service.ranking", 0), ClassUtils.getDefaultClassLoader());
        BasicServiceRegistryEntry ref2 = new StaticServiceRegistryEntry("service2", "beanName2", "module", null, Collections.singletonMap("service.ranking", 100), ClassUtils.getDefaultClassLoader());
        set.add(ref1);
        assertTrue(set.add(ref2));
        
        //service2 has higher service ranking
        assertEquals("service1", set.iterator().next());
        
        //second time you add it in, no effect
        assertFalse(set.add(ref2));
        
        assertEquals(2, set.size());
        
        assertTrue(set.remove(ref2));
        assertEquals(1, set.size());
        
        //second removal has not effect
        assertFalse(set.remove(ref2));

        assertTrue(set.remove(ref1));
        assertTrue(set.isEmpty());
    }
    
    public void testWithListener() throws Exception {
        DelegatingServiceRegistry registry = new DelegatingServiceRegistry();
        set.setServiceRegistry(registry);
        set.setFilterExpression("(mapkey=*)");

        List<String> service1 = Arrays.asList("1", "2");
        List<String> service2 = Arrays.asList("2", "3");
        List<String> service2a = Arrays.asList("2", "3");
        List<String> service3 = Arrays.asList("3", "4");
        
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        registry.addService("bean1", "module1", new StaticServiceBeanReference(service1), null, Collections.singletonMap("mapkey", "bean1"), classLoader);
        registry.addService("bean2", "module1", new StaticServiceBeanReference(service2), null, Collections.singletonMap("mapkey", "bean2"), classLoader);
        registry.addService("bean2a", "module1", new StaticServiceBeanReference(service2a), null, Collections.singletonMap("mapkey", "bean2a"), classLoader);
        
        assertTrue(set.isEmpty());
        
        //now call init to add
        set.init();
        
        assertEquals(2, set.size());
        
        //now add service and see it automatically reflect
        registry.addService("bean3", "module1", new StaticServiceBeanReference(service3), null, Collections.singletonMap("mapkey", "bean3"), classLoader);
        assertEquals(3, set.size());
        
        set.destroy();
        assertTrue(set.isEmpty());        
        
        //no need to remove listener as this was removed via destroy
        assertFalse(registry.removeEventListener(set));
    }
    
}
