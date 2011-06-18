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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;

import junit.framework.TestCase;

public class SimpleGraphRootModuleDefinitionTest extends TestCase {

    public void testSimpleGraphRootModuleDefinition() {
        

        final ModuleDefinition sibling = new SimpleModuleDefinition("sibling");
        final List<ModuleDefinition> siblings = Collections.singletonList(sibling);
        final List<String> dependencies = Arrays.asList("mod-a", "mod-b");
        
        SimpleRootModuleDefinition root = newRootModuleDefinition(siblings, dependencies);
        
        assertEquals(root.getSiblings(), siblings);
        assertEquals(root.getDependentModuleNames(), dependencies);
        
        SimpleModuleDefinition child1OfSibling = new SimpleModuleDefinition(sibling, "child1OfSibling");
        SimpleModuleDefinition child2OfSibling = new SimpleModuleDefinition(child1OfSibling, "child2OfSibling");
        SimpleModuleDefinition childOfRoot = new SimpleModuleDefinition(root, "childOfRoot");
        
        assertSame(child1OfSibling, root.findChildDefinition("child1OfSibling", true));
        assertSame(child2OfSibling, root.findChildDefinition("child2OfSibling", true));
        assertSame(childOfRoot, root.findChildDefinition("childOfRoot", true));
        
        assertSame(child1OfSibling, root.findChildDefinition("child1OfSib", false));
        assertSame(child2OfSibling, root.findChildDefinition("child2OfSib", false));
        assertSame(childOfRoot, root.findChildDefinition("childOfRoo", false));

        assertNull(root.findChildDefinition("childOfNothing", false));
    }
    
    public void testSiblingEquality() {

        final List<String> dependencies = Arrays.asList("mod-a", "mod-b");
        
        final ModuleDefinition sibling1 = new SimpleModuleDefinition("sibling1");
        final List<ModuleDefinition> siblings1 = Collections.singletonList(sibling1);
        SimpleRootModuleDefinition root1 = newRootModuleDefinition(siblings1, dependencies);
        
        final ModuleDefinition sibling2 = new SimpleModuleDefinition("sibling2");
        final List<ModuleDefinition> siblings2 = Collections.singletonList(sibling2);
        SimpleRootModuleDefinition root2 = newRootModuleDefinition(siblings2, dependencies);
        
        //different siblings do not imply not equals
        assertEquals(root1, root2); 

        //different dependencies do imply not equals
        final List<String> dependencies3 = Arrays.asList("mod-a", "mod-b", "mod-c");
        SimpleRootModuleDefinition root3 = newRootModuleDefinition(siblings1, dependencies3);
        assertFalse(root1.equals(root3));
    }

    private SimpleRootModuleDefinition newRootModuleDefinition(final List<ModuleDefinition> siblings, List<String> dependencies) {
        final String[] locations = {"c1.xml", "c2.xml"};
        
        SimpleRootModuleDefinition root = 
            new SimpleRootModuleDefinition("project1",
                    locations, 
                    dependencies.toArray(new String[0]), 
                    null, 
                    siblings.toArray(new ModuleDefinition[0]), null);
        
        return root;
    }

}
