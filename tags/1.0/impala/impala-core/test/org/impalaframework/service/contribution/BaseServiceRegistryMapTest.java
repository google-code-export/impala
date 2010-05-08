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

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.reference.BasicServiceRegistryEntry;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class BaseServiceRegistryMapTest extends TestCase {
    
    private BaseServiceRegistryMap map;

    @Override
    protected void setUp() throws Exception {
        super.setUp();        
        map = new BaseServiceRegistryMap() {

            @Override
            protected Object maybeGetProxy(ServiceRegistryEntry entry) {
                return entry.getServiceBeanReference().getService();
            }
            
        };
    }

    public void testAddRemove() throws Exception {

        BasicServiceRegistryEntry ref1 = new StaticServiceRegistryEntry("service1", "beanName1", "module", null, Collections.singletonMap("mapkey", "bean1"), ClassUtils.getDefaultClassLoader());
        BasicServiceRegistryEntry ref2 = new StaticServiceRegistryEntry("service2", "beanName2", "module", null, Collections.singletonMap("mapkey", "bean2"), ClassUtils.getDefaultClassLoader());
        assertTrue(map.add(ref1));
        assertTrue(map.add(ref2));

        assertTrue(map.add(ref1));
        assertTrue(map.add(ref2));
        
        assertEquals(2, map.size());
        
        assertEquals("service1", map.get("bean1"));
        assertEquals("service2", map.get("bean2"));
        
        BasicServiceRegistryEntry refWithNoMapKey = new StaticServiceRegistryEntry("service2", "beanName2", "module", null, null, ClassUtils.getDefaultClassLoader());

        assertFalse(map.add(refWithNoMapKey));
        assertEquals(2, map.size());
        
        assertTrue(map.remove(ref1));
        assertFalse(map.remove(ref1));
        
        assertEquals(1, map.size());
        
        assertTrue(map.remove(ref2));
        assertTrue(map.isEmpty());
    }
}
