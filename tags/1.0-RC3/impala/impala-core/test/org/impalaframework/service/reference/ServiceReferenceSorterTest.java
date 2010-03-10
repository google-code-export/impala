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

package org.impalaframework.service.reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryEntry;
import org.springframework.util.ClassUtils;

public class ServiceReferenceSorterTest extends TestCase {
    
    private ServiceReferenceSorter sorter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sorter = new ServiceReferenceSorter();
    }

    public void testSort() {
        ServiceRegistryEntry ref1 = newRef(new Integer(1));
        ServiceRegistryEntry ref2 = newRef("nothing");
        ServiceRegistryEntry ref3 = newRef(-100);
        ServiceRegistryEntry ref4 = newRef(400);
        ServiceRegistryEntry ref5 = newRef(Long.MAX_VALUE);
        ServiceRegistryEntry ref6 = newRef(new Double(Integer.MAX_VALUE + ".51"));
        
        List<ServiceRegistryEntry> list = Arrays.asList(ref1, ref2, ref3, ref4, ref5, ref6);
        List<ServiceRegistryEntry> sorted = sorter.sort(list);
        List<ServiceRegistryEntry> expected = Arrays.asList(ref5, ref6, ref4, ref1, ref2, ref3);
        System.out.println("sorted:" + sorted);
        System.out.println("expected:" + expected);
        assertEquals(expected, sorted);
    }

    private ServiceRegistryEntry newRef(Object ranking) {
        Map<String, Object> attributes = null;
        if (ranking != null) {
            attributes = new HashMap<String, Object>();
            attributes.put("service.ranking", ranking);
        }
        ServiceRegistryEntry entry = new StaticServiceRegistryEntry("service", "bean", "mod", null, attributes, ClassUtils.getDefaultClassLoader());
        return entry;
    }

}
