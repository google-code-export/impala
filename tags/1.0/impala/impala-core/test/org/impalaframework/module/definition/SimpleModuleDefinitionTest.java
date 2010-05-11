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
import java.util.List;
import java.util.Map;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.ModuleState;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class SimpleModuleDefinitionTest extends TestCase {

    public void testSimpleModuleDefinition() {

        SimpleModuleDefinition definition = new SimpleModuleDefinition("p1");

        assertEquals(0, definition.getConfigLocations().size());
        SimpleModuleDefinition child1 = new SimpleModuleDefinition(definition, "c1");
        SimpleModuleDefinition child2 = new SimpleModuleDefinition(definition, "c2");
        assertTrue(definition.hasChildModuleDefinition("c1"));
        assertTrue(definition.hasChildModuleDefinition("c2"));
        assertEquals(2, definition.getChildModuleDefinitions().size());
        assertEquals(2, definition.getChildModuleDefinitions().size());

        assertSame(child1, definition.getChildModuleDefinition("c1"));
        assertSame(child2, definition.getChildModuleDefinition("c2"));
    }
    
    public void testSetState() throws Exception {       

        SimpleModuleDefinition definition = new SimpleModuleDefinition("p1");
        definition.setState("mystate");
        assertEquals("mystate", definition.getState());
        
        definition.freeze();
        definition.setState(ModuleState.ERROR);
        assertEquals(ModuleState.ERROR, definition.getState());
        System.out.println(definition.toString());
        
        try {
            definition.setState("other");
            fail();
        } catch (InvalidStateException e) {
            assertEquals("Cannot change object 'name=p1, configLocations=[], type=APPLICATION, dependencies=[], runtime=spring, state=ERROR' as this has been frozen", e.getMessage());
        }
    }

    public void testEquals() {

        SimpleModuleDefinition definition1a = new SimpleModuleDefinition(null, "p1", new String[] { "loc1", "loc2" });
        SimpleModuleDefinition definition1b = new SimpleModuleDefinition(null, "p1", new String[] { "loc1", "loc2" });
        SimpleModuleDefinition definition1c = new SimpleModuleDefinition(null, "p1", new String[] { "loc1", });

        assertEquals(definition1a, definition1b);
        assertFalse(definition1a.equals(definition1c));
        
        //different types
        SimpleModuleDefinition definition1d = new SimpleModuleDefinition(null, "p1", "servlet", new String[] { "loc1", }, null, null, null);
        SimpleModuleDefinition definition1e = new SimpleModuleDefinition(null, "p1", "webroot", new String[] { "loc1", }, null, null, null);
        assertFalse(definition1d.equals(definition1e));

        //different attributes
        SimpleModuleDefinition definition1f = new SimpleModuleDefinition(null, "p1", "servlet", new String[] { "loc1", }, null, Collections.singletonMap("name", "value1"), null);
        SimpleModuleDefinition definition1g = new SimpleModuleDefinition(null, "p1", "servlet", new String[] { "loc1", }, null, Collections.singletonMap("name", "value2"), null);
        assertFalse(definition1f.equals(definition1g));
    }
    
    public void testAttributes() throws Exception {
        SimpleModuleDefinition definition = new SimpleModuleDefinition(null, "p1", ModuleTypes.APPLICATION, null, null, Collections.singletonMap("name", "value"), null);
        final Map<String, String> attributes = definition.getAttributes();
        assertEquals(1, attributes.size());
    }

    public void testConstructors() {

        SimpleModuleDefinition definition1a = new SimpleModuleDefinition(null, "p1", new String[] { "loc1" });
        SimpleModuleDefinition definition1b = new SimpleModuleDefinition(null, "p1", new String[] {});
        SimpleModuleDefinition definition1c = new SimpleModuleDefinition(null, "p1", null);

        checkLocation(definition1a, "loc1");
        assertTrue(definition1b.getConfigLocations().isEmpty());
        assertTrue(definition1c.getConfigLocations().isEmpty());
    }

    private void checkLocation(SimpleModuleDefinition definition, String expected) {
        List<String> configLocations = definition.getConfigLocations();
        assertEquals(1, configLocations.size());
        assertEquals(expected, configLocations.get(0));
    }

}
