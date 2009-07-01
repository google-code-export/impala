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

package org.impalaframework.web.integration;

import org.impalaframework.exception.InvalidStateException;

import junit.framework.TestCase;

public class PrefixTreeHolderTest extends TestCase {

    public void testAdd() {
        PrefixTreeHolder holder = new PrefixTreeHolder();
        holder.add("module1", "/m1");
        holder.add("module1", "/m2");
        
        holder.add("module2", "/m1plus");
        
        try {
            holder.add("module", "/m2");
            fail();
        }
        catch (InvalidStateException e) {
            assertEquals("Module 'module' cannot use key '/m2', as it is already being used by module 'module1'", e.getMessage());
        }
        
        assertEquals(2, holder.unloadForModule("module1"));
    }

}
