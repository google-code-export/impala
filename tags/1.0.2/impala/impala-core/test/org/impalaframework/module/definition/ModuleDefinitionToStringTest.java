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

import org.impalaframework.module.RootModuleDefinition;

import junit.framework.TestCase;

public class ModuleDefinitionToStringTest extends TestCase {

    public void testToString() throws Exception {
        RootModuleDefinition definition = new SimpleRootModuleDefinition("project1", new String[] { "location1.xml",
                "location2.xml" });
        SimpleModuleDefinition module1 = new SimpleModuleDefinition(definition, "module1");
        new SimpleModuleDefinition(definition, "module2", new String[] {
                "module2-1.xml", "module1-2.xml" });
        new SimpleModuleDefinition(definition, "module3", new String[] {
                "module3-1.xml", "module3-2.xml"});
        
        new SimpleModuleDefinition(module1, "module4");
        new SimpleModuleDefinition(module1, "module5");
        
        String output = definition.toString();
        System.out.println(output);
        
        String lineSeparator = System.getProperty("line.separator");
        String expected = 
        "name=project1, configLocations=[location1.xml, location2.xml], type=ROOT, dependencies=[], runtime=spring" + lineSeparator +
        "  name=module1, configLocations=[], type=APPLICATION, dependencies=[project1], runtime=spring" +   lineSeparator+
        "    name=module4, configLocations=[], type=APPLICATION, dependencies=[module1], runtime=spring" + lineSeparator+
        "    name=module5, configLocations=[], type=APPLICATION, dependencies=[module1], runtime=spring" + lineSeparator+
        "  name=module2, configLocations=[module2-1.xml, module1-2.xml], type=APPLICATION, dependencies=[project1], runtime=spring" + lineSeparator+
        "  name=module3, configLocations=[module3-1.xml, module3-2.xml], type=APPLICATION, dependencies=[project1], runtime=spring";
        
        assertEquals(expected, output);
    }

}
