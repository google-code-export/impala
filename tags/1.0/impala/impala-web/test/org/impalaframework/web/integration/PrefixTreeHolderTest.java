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

package org.impalaframework.web.integration;

import org.impalaframework.exception.InvalidStateException;

import junit.framework.TestCase;

public class PrefixTreeHolderTest extends TestCase {
    
    private PrefixTreeHolder holder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        holder = new PrefixTreeHolder();
    }
    
    public void testWebFrameworks() throws Exception {
        holder.add("webframeworks-wicket", "/wicket/aa", null, null);
        holder.add("webframeworks-wicket", "/wicket/aaa", null, null);
        holder.add("webframeworks-tapestry5", "/Tapestry/bbb", null, null);

        assertEquals(new ModuleNameWithPath("webframeworks-wicket"), holder.getModuleForURI("/wicket/aabbb").getValue());
        assertEquals(null, holder.getModuleForURI("/webframeworks-web/Tapestry5Home"));
    }

    public void testAdd() {
        holder.add("module1", "/m1", null, null);
        holder.add("module1", "/m2", null, null);
        
        holder.add("module2", "/m1plus", null, null);
        
        try {
            holder.add("module", "/m2", null, null);
            fail();
        }
        catch (InvalidStateException e) {
            assertEquals("Module 'module' cannot use key '/m2', as it is already being used by module 'module1'", e.getMessage());
        }
        
        assertEquals(2, holder.unloadForModule("module1"));
        
        //check stuff is gone
        assertFalse(holder.getTrie().contains("/m2"));
        assertNull(holder.getContributions().get("module1"));
    }
    
    public void testAddRemove() throws Exception {

        holder.add("module1", "/m1", null, null);
        holder.add("module1", "/m2", null, null);
        assertTrue(holder.getTrie().contains("/m2"));
        
        holder.add("module2", "/m1plus", null, null);
        
        assertTrue(holder.remove("module1", "/m1"));
        
        //belongs to module 1
        assertFalse(holder.remove("module1", "/m1plus"));
        assertTrue(holder.remove("module1", "/m2"));
        
        //gone already
        assertFalse(holder.remove("module1", "/m2"));

        //check stuff is gone
        assertFalse(holder.getTrie().contains("/m2"));
        assertNull(holder.getContributions().get("module1"));
    }

}
