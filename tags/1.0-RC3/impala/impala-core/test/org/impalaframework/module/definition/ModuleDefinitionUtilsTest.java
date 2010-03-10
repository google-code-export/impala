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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;

public class ModuleDefinitionUtilsTest extends TestCase {

    public final void testFindModule() {
        
        SimpleModuleDefinition definition = new SimpleModuleDefinition("p1");

        assertEquals(0, definition.getConfigLocations().size());
        SimpleModuleDefinition child1 = new SimpleModuleDefinition(definition, "c1-full");
        SimpleModuleDefinition child2 = new SimpleModuleDefinition(definition, "c2");
        SimpleModuleDefinition child3 = new SimpleModuleDefinition(child2, "c3");
        
        assertSame(definition.findChildDefinition("c1-full", true), child1);
        assertSame(definition.findChildDefinition("c2", true), child2);
        assertSame(definition.findChildDefinition("c3", true), child3);
        assertNull(definition.findChildDefinition("c4", true));
        
        assertSame(definition.findChildDefinition("c1-full", true), child1);
        assertSame(definition.findChildDefinition("c1", false), child1);
        
        assertSame(child2.findChildDefinition("c3", true), child3);
        assertSame(child2.findChildDefinition("3", false), child3);
        assertNull(child2.findChildDefinition("c4", true));
    }
    
    public void testGetDependentModules() throws Exception {
        
        SimpleRootModuleDefinition a = new SimpleRootModuleDefinition("a", (String)null);
        ModuleDefinition a1 = newDefinition(a, "a1", null);
        ModuleDefinition a2 = newDefinition(a1, "a2", null);
        ModuleDefinition a3 = newDefinition(a2, "a3", null);
        newDefinition(a2, "a4", "b2");
        newDefinition(a3, "a5", "b3");
        
        ModuleDefinition b1 = newDefinition(null, "b1", null);
        ModuleDefinition b2 = newDefinition(b1, "b2", null);
        newDefinition(b2, "b3", null);
        a.addSibling(b1);
        
        ModuleDefinition c1 = newDefinition(null, "c1", "a3");
        ModuleDefinition c2 = newDefinition(c1, "c2", null);
        newDefinition(c2, "c3", null);
        a.addSibling(c1);

        doGetDependentModules(a, "a2", "a3,a4,a5,c1,c2,c3");
        doGetDependentModules(a, "b2", "b3,a4,a5");
        doGetDependentModules(a, "a3", "a5,c1,c2,c3");
        doGetDependentModules(a, "c3", null);
        doGetDependentModules(a, "a5", null);
    }

    private void doGetDependentModules(RootModuleDefinition root, String name, String expected) {
        final Collection<ModuleDefinition> dependentModules = ModuleDefinitionUtils.getDependentModules(root, name);
        final List<String> names = ModuleDefinitionUtils.getModuleNamesFromCollection(dependentModules);
        System.out.println(names);
        assertEquals(expected != null ? Arrays.asList(expected.split(",")) : new ArrayList<String>(), names);
    }
    
    private ModuleDefinition newDefinition(ModuleDefinition parent, final String name, String dependencies) {
        ModuleDefinition definition = new SimpleModuleDefinition(parent, name, ModuleTypes.APPLICATION, null, dependencies == null ? new String[0] : dependencies.split("'"), null, null);
        return definition;
    }
    
    public void testGetModuleNames() throws Exception {
        List<ModuleDefinition> moduleDefinitions = Arrays.asList(new ModuleDefinition[]{new SimpleModuleDefinition("m1"), new SimpleModuleDefinition("m2")});
        assertEquals(Arrays.asList("m1","m2"), ModuleDefinitionUtils.getModuleNamesFromCollection(moduleDefinitions));

        assertNotNull(ModuleDefinitionUtils.getModuleFromCollection(moduleDefinitions, "m1"));
        assertNotNull(ModuleDefinitionUtils.getModuleFromCollection(moduleDefinitions, "m2"));
        assertNull(ModuleDefinitionUtils.getModuleFromCollection(moduleDefinitions, "someother"));
    }


}
