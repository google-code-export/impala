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

package org.impalaframework.module.definition;

import java.util.Collections;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class SimpleRootModuleDefinitionTest extends TestCase {

    private String rootModuleName = "project1";
    
    public void testParent() {
        SimpleRootModuleDefinition definition = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"});
        assertEquals("project1", definition.getName());
        assertNull(definition.getParentDefinition());
        
        SimpleModuleDefinition child1 = new SimpleModuleDefinition(definition, "c1");
        SimpleModuleDefinition child2 = new SimpleModuleDefinition(definition, "c2");
        assertTrue(definition.hasChildModuleDefinition("c1"));
        assertTrue(definition.hasChildModuleDefinition("c2"));
        assertEquals(2, definition.getChildModuleDefinitions().size());
        assertEquals(2, definition.getChildModuleDefinitions().size());
        
        assertSame(child1, definition.getChildModuleDefinition("c1"));
        assertSame(child2, definition.getChildModuleDefinition("c2"));
    }
    
    public void testAttributes() throws Exception {
        SimpleRootModuleDefinition definition = new SimpleRootModuleDefinition("p1", null, null, Collections.singletonMap("name", "value"), null, null);
        final Map<String, String> attributes = definition.getAttributes();
        assertEquals(1, attributes.size());
    }
    
    public void testDefaultContextLocations() {
        assertTrue(new SimpleRootModuleDefinition(rootModuleName, new String[0]).getConfigLocations().isEmpty());
    }

    public void testEquals() {
        SimpleRootModuleDefinition spec1 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"});
        SimpleRootModuleDefinition spec2 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"});
        assertEquals(spec1, spec2);
        SimpleRootModuleDefinition spec3 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1"});
        SimpleRootModuleDefinition spec4 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p3"});
        assertFalse(spec1.equals(spec3));
        assertFalse(spec1.equals(spec4));
        
        SimpleRootModuleDefinition spec5 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"}, new String[]{"dep1", "dep2"}, null, null, null);
        SimpleRootModuleDefinition spec6 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"}, new String[]{"dep1", "dep2"}, null, null, null);
        assertEquals(spec5, spec6);
        SimpleRootModuleDefinition spec7 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1"}, new String[]{"dep1"}, null, null, null);
        SimpleRootModuleDefinition spec8 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p3"}, new String[]{"dep2"}, null, null, null);
        assertFalse(spec5.equals(spec7));
        assertFalse(spec5.equals(spec8));
    }
    
    public void testAddSibling() {
        SimpleRootModuleDefinition spec1 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1"});
        spec1.addSibling(new SimpleModuleDefinition("sibling1"));
        assertNotNull(spec1.findChildDefinition("sibling1", true));
        assertNotNull(spec1.getSiblingModule("sibling1"));
    }

}
