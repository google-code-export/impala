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

package org.impalaframework.module.type;

import org.impalaframework.exception.ConfigurationException;

import junit.framework.TestCase;

public class TypeReaderRegistryTest extends TestCase {

    private TypeReaderRegistry registry;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        registry = new TypeReaderRegistry();
    }

    public void testGetTypeReader() {
        try {
            registry.getTypeReader("mytype");
            fail();
        } catch (ConfigurationException e) {
            assertEquals("No type reader available for module type 'mytype', and no default module type reader has been set", e.getMessage());
        }
        
        final ApplicationModuleTypeReader typeReader = new ApplicationModuleTypeReader();
        registry.setDefaultTypeReader(typeReader);
        assertSame(typeReader, registry.getTypeReader("mytype"));
    }

}
