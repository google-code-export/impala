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

import java.util.Collections;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceRegistryEntry;
import org.springframework.util.ClassUtils;

public class ServiceRegistryEntryTest extends TestCase {

    public void testConstruct() throws Exception {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        ServiceRegistryEntry entry = new StaticServiceRegistryEntry("service1","beanName",
                "moduleName", classLoader);
        assertEquals(0, entry.getAttributes().size());
        assertSame(classLoader, entry.getBeanClassLoader());
        assertTrue(entry.getExportTypes().isEmpty());
    }
    
    public void testConstructAttributes() throws Exception {
        ServiceRegistryEntry entry = new StaticServiceRegistryEntry("service1","beanName",
                "moduleName", null, Collections.singletonMap("attribute","value"), ClassUtils.getDefaultClassLoader());
        assertEquals(1, entry.getAttributes().size());
    }

    public void testConstructTagsAttributesNull() throws Exception {
        ServiceRegistryEntry entry = new StaticServiceRegistryEntry("service1","beanName",
                "moduleName", null, null, ClassUtils.getDefaultClassLoader());
        assertEquals(0, entry.getAttributes().size());
    }   
}
