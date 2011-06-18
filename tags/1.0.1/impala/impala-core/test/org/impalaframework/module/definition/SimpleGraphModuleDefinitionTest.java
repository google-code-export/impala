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

import java.util.List;

import org.impalaframework.module.ModuleDefinition;

import junit.framework.TestCase;

public class SimpleGraphModuleDefinitionTest extends TestCase {

    public void testGetDependentModuleNames() {
        ModuleDefinition newC = new SimpleModuleDefinition(null, "module-c", ModuleTypes.APPLICATION, null, new String[] {"module-a"}, null, null);
        
        final List<String> cNames = newC.getDependentModuleNames();
        assertEquals(1, cNames.size());
        
        //and e, with c as parent, and depending also on b
        ModuleDefinition newE = new SimpleModuleDefinition(newC, "module-e", ModuleTypes.APPLICATION, null, new String[] {"module-b", "module-d"}, null, null);
        
        //note how parent is implicitly first - appears first in list
        final List<String> eNames = newE.getDependentModuleNames();
        assertEquals(3, eNames.size());
        assertEquals("module-c", eNames.get(0));
        assertEquals("module-b", eNames.get(1));
        assertEquals("module-d", eNames.get(2));

        //parent is named explicitly as module: note its position in ordering
        ModuleDefinition newF = new SimpleModuleDefinition(newE, "module-f", ModuleTypes.APPLICATION, null, new String[] {"module-d", "module-e"}, null, null);

        final List<String> fNames = newF.getDependentModuleNames();
        assertEquals(2, fNames.size());
        assertEquals("module-d", fNames.get(0));
        assertEquals("module-e", fNames.get(1));
    }

}
