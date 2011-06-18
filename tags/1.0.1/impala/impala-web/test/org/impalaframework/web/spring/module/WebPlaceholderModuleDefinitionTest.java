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

package org.impalaframework.web.spring.module;

import junit.framework.TestCase;

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.web.module.WebModuleTypes;

public class WebPlaceholderModuleDefinitionTest extends TestCase {
    
    private String projectName = "impala-core";
    
    public void testGetters() throws Exception {
        RootModuleDefinition parent = new SimpleRootModuleDefinition(projectName, "parent-context.xml");
        WebPlaceholderModuleDefinition definition1 = new WebPlaceholderModuleDefinition(parent, "placeholder");
        assertEquals("placeholder", definition1.getName());
        assertEquals(WebModuleTypes.WEB_PLACEHOLDER, definition1.getType());
        assertSame(parent, definition1.getParentDefinition());
        assertTrue(definition1.getConfigLocations().isEmpty());
    }   
    
    public void testEquals() throws Exception {
        RootModuleDefinition parent = new SimpleRootModuleDefinition(projectName, "parent-context.xml");
        WebPlaceholderModuleDefinition definition1 = new WebPlaceholderModuleDefinition(parent, "placeholder");
        WebPlaceholderModuleDefinition definition2 = new WebPlaceholderModuleDefinition(parent, "placeholder");
        
        assertEquals(definition1, definition2);
    }
    
    public void testAdd() throws Exception {
        RootModuleDefinition parent = new SimpleRootModuleDefinition(projectName, "parent-context.xml");
        WebPlaceholderModuleDefinition definition1 = new WebPlaceholderModuleDefinition(parent, "placeholder");
        WebPlaceholderModuleDefinition definition3 = new WebPlaceholderModuleDefinition(parent, "toAdd");
        
        try {
            definition1.addChildModuleDefinition(definition3);
            fail();
        }
        catch (UnsupportedOperationException e) {
            assertEquals("Cannot add module 'toAdd' to web placeholder module definitionSource 'placeholder', as this cannot contain other modules", e.getMessage());
        }
        
        assertNull(definition1.findChildDefinition("someother", true));
        assertTrue(definition1.getChildModuleDefinitions().isEmpty());
        assertTrue(definition1.getChildModuleNames().isEmpty());
        assertNull(definition1.removeChildModuleDefinition("someplugin"));
    }
    
}
