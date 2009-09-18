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

package org.impalaframework.module.definition;

import static org.impalaframework.classloader.graph.GraphTestUtils.assertModules;
import static org.impalaframework.classloader.graph.GraphTestUtils.newDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;


public class DependencyManagerGraphTest extends TestCase {
    
    private SimpleRootModuleDefinition rootDefinition;
    private DependencyManager manager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        manager = new DependencyManager(rootDefinition);
        manager.unfreeze();
    }
    
    public void testGetDirectDependees() throws Exception {
        rootDefinition = definitionSet1();
        Collection<ModuleDefinition> allModules = manager.getAllModules();
        assertModules("root,b,c,d", allModules);
    }

    private SimpleRootModuleDefinition definitionSet1() {
        List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
        
        //root has no sibling or dependencies
        SimpleRootModuleDefinition a = new SimpleRootModuleDefinition(
                "root", 
                new String[] {"root.xml"}, 
                null, 
                null, 
                null, 
                null);
        
        //b is child of a
        ModuleDefinition b = newDefinition(definitions, a, "b", null);
        
        //c is child of a
        ModuleDefinition c = newDefinition(definitions, a, "c", null);
        
        //d is child of b but depends on b
        ModuleDefinition d = newDefinition(definitions, b, "d", "c");
        
        return a;
    }
    
}
