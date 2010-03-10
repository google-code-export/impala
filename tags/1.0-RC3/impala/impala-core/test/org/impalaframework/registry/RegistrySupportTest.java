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

package org.impalaframework.registry;

import junit.framework.TestCase;

public class RegistrySupportTest extends TestCase {
    
    public void testRemove() throws Exception {
        RegistrySupport registry = new RegistrySupport();
        registry.addRegistryItem("key", "value");
        assertEquals("value", registry.getEntry("key", String.class));
        assertEquals("value", registry.removeEntry("key"));
        assertEquals(null, registry.getEntry("key", String.class, false));
        assertEquals(null, registry.removeEntry("key"));
    }
    
    public void testConcurrent() throws Exception {
        ConcurrentRegistrySupport registry = new ConcurrentRegistrySupport();
        registry.addRegistryItem("key", "value");
        assertEquals("value", registry.getEntry("key", String.class));
    }
    
}
