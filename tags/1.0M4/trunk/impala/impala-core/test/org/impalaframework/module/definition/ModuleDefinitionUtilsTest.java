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

import java.util.Arrays;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.SimpleModuleDefinition;

import junit.framework.TestCase;

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
	
	public void testGetModuleNames() throws Exception {
		List<ModuleDefinition> moduleDefinitions = Arrays.asList(new ModuleDefinition[]{new SimpleModuleDefinition("m1"), new SimpleModuleDefinition("m2")});
		assertEquals(Arrays.asList("m1","m2"), ModuleDefinitionUtils.getModuleNamesFromCollection(moduleDefinitions));

		assertNotNull(ModuleDefinitionUtils.getModuleFromCollection(moduleDefinitions, "m1"));
		assertNotNull(ModuleDefinitionUtils.getModuleFromCollection(moduleDefinitions, "m2"));
		assertNull(ModuleDefinitionUtils.getModuleFromCollection(moduleDefinitions, "someother"));
	}


}
