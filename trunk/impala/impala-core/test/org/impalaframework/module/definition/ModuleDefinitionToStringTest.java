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

import junit.framework.TestCase;

public class ModuleDefinitionToStringTest extends TestCase {

	public void testToString() throws Exception {
		RootModuleDefinition definition = new SimpleRootModuleDefinition("project1", new String[] { "location1.xml",
				"location2.xml" });
		SimpleModuleDefinition module1 = new SimpleModuleDefinition(definition, "module1");
		new SimpleModuleDefinition(definition, "module2", new String[] {
				"module2-1.xml", "module1-2.xml" });
		new SimpleBeansetModuleDefinition(definition, "module3", new String[] {
				"module3-1.xml", "module3-2.xml"}, "main: alternative");
		
		new SimpleModuleDefinition(module1, "module4");
		new SimpleModuleDefinition(module1, "module5");
		
		String output = definition.toString();
		System.out.println(output);
		
		String lineSeparator = System.getProperty("line.separator");
		String expected = 
		"name=project1, contextLocations=[location1.xml, location2.xml], type=ROOT, dependencies=[]" + lineSeparator +
		"  name=module1, contextLocations=[module1-context.xml], type=APPLICATION, dependencies=[project1]" +   lineSeparator+
		"    name=module4, contextLocations=[module4-context.xml], type=APPLICATION, dependencies=[module1]" + lineSeparator+
		"    name=module5, contextLocations=[module5-context.xml], type=APPLICATION, dependencies=[module1]" + lineSeparator+
		"  name=module2, contextLocations=[module2-1.xml, module1-2.xml], type=APPLICATION, dependencies=[project1]" + lineSeparator+
		"  name=module3, contextLocations=[module3-1.xml, module3-2.xml], type=APPLICATION_WITH_BEANSETS, dependencies=[project1], overrides = {main=[alternative]}";
		
		assertEquals(expected, output);
	}

}
