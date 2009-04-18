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

package org.impalaframework.service.registry;

import java.util.Collections;

import org.impalaframework.service.ServiceRegistryReference;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class ServiceReferenceTest extends TestCase {

    public void testConstruct() throws Exception {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        ServiceRegistryReference serviceReference = new BasicServiceRegistryReference("service1","beanName",
                "moduleName", classLoader);
        assertEquals(0, serviceReference.getAttributes().size());
        assertSame(classLoader, serviceReference.getBeanClassLoader());
        assertTrue(serviceReference.getExportedTypes().isEmpty());
    }
    
    public void testConstructAttributes() throws Exception {
        ServiceRegistryReference serviceReference = new BasicServiceRegistryReference("service1","beanName",
                "moduleName", null, Collections.singletonMap("attribute","value"), ClassUtils.getDefaultClassLoader());
        assertEquals(1, serviceReference.getAttributes().size());
    }

    public void testConstructTagsAttributesNull() throws Exception {
        ServiceRegistryReference serviceReference = new BasicServiceRegistryReference("service1","beanName",
                "moduleName", null, null, ClassUtils.getDefaultClassLoader());
        assertEquals(0, serviceReference.getAttributes().size());
    }   
}
