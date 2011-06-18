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

package org.impalaframework.module.modification.graph;

import static org.impalaframework.classloader.graph.GraphTestUtils.newDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.spi.ModuleStateChange;

public class StickyGraphModificationExtractorTest extends TestCase {
    
    private GraphModificationExtractorDelegate graphModificationExtractor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        graphModificationExtractor = new StickyGraphModificationExtractorDelegate();
    }
    
    public void testNullToRoot1() throws Exception {
        SimpleRootModuleDefinition root1 = definitionSet1();
        assertTransitions(null, root1, null, "a,c,d,root,b,e,f,g");
    }
    
    public void testNullToRoot2() throws Exception {
        SimpleRootModuleDefinition root2 = definitionSet2();
        assertTransitions(null, root2, null, "a,c,root,e,f");
    }
    
    public void testRoot1ToNull() throws Exception {
        SimpleRootModuleDefinition root1 = definitionSet1();
        assertTransitions(root1, null, "g,f,e,b,root,d,c,a", null);
    }
    
    public void testRoot2ToNull() throws Exception {
        SimpleRootModuleDefinition root2 = definitionSet2();
        assertTransitions(root2, null, "f,e,root,c,a", null);
    }

    public void testRoot1ToRoot2() throws Exception {
        SimpleRootModuleDefinition root1 = definitionSet1();
        SimpleRootModuleDefinition root2 = definitionSet2();
        
        assertTransitions(root1, root2, "b,d", null);
    }
    
    public void testRoot2ToRoot1() throws Exception {
        SimpleRootModuleDefinition root1 = definitionSet1();
        SimpleRootModuleDefinition root2 = definitionSet2();
        
        assertTransitions(root2, root1, null, "d,b,g");
    }
    
    private SimpleRootModuleDefinition definitionSet1() {
        List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
        
        //a has no parent or dependencies
        ModuleDefinition a = newDefinition(definitions, null, "a", null);
        
        //b depends on d but has no parent
        ModuleDefinition b = newDefinition(definitions, null, "b", "d");
        
        //c has no parent or dependencies
        ModuleDefinition c = newDefinition(definitions, null, "c", null);
        
        //d has no parent or dependencies
        ModuleDefinition d = newDefinition(definitions, null, "d", null);
        
        //root has siblings a to d, and depends on a
        SimpleRootModuleDefinition root = new SimpleRootModuleDefinition("root", 
                new String[] {"root.xml"}, 
                new String[] {"a"}, 
                null,
                new ModuleDefinition[] {a, b, c, d}, null);
        //e has parent root, and depends on b an d
        ModuleDefinition e = newDefinition(definitions, root, "e", "c");
        
        //has parent e, and depends on c
        newDefinition(definitions, e, "f", "c");

        //has parent e, depends on f
        newDefinition(definitions, e, "g", "f");
        return root;
    }
    
    private SimpleRootModuleDefinition definitionSet2() {
        List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();

        //a same as set 1
        ModuleDefinition a = newDefinition(definitions, null, "a", null);

        //c same as set 1
        ModuleDefinition c = newDefinition(definitions, null, "c", null);
        
        //root has siblings a and c, and depends on a (same as set 1)
        SimpleRootModuleDefinition root = new SimpleRootModuleDefinition("root", 
                new String[] {"root.xml"}, 
                new String[] {"a"}, 
                null,
                new ModuleDefinition[] {a, c}, null);
        
        //e has parent root, and depends on a and c
        ModuleDefinition e = newDefinition(definitions, root, "e", "c");
        
        //f has same form as set 1
        newDefinition(definitions, e, "f", "c");
        return root;
    }
    
    private Collection<? extends ModuleStateChange> assertTransitions(SimpleRootModuleDefinition root1,
            SimpleRootModuleDefinition root2, String expectedUnloads, String expectedLoads) {
        return GraphModificationTestUtils.assertTransitions(graphModificationExtractor, root1, root2, expectedUnloads, expectedLoads);
    }
}
