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

import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.SimpleModuleDefinition;

import junit.framework.TestCase;

public class ModuleDefinitionUtilsTest extends TestCase {

	public final void testFindPlugin() {
		
		SimpleModuleDefinition definition = new SimpleModuleDefinition("p1");

		assertEquals(1, definition.getContextLocations().size());
		assertEquals("p1-context.xml", definition.getContextLocations().get(0));
		SimpleModuleDefinition child1 = new SimpleModuleDefinition(definition, "c1-full");
		SimpleModuleDefinition child2 = new SimpleModuleDefinition(definition, "c2");
		SimpleModuleDefinition child3 = new SimpleModuleDefinition(child2, "c3");
		
		assertSame(ModuleDefinitionUtils.findDefinition("c1-full", definition, true), child1);
		assertSame(ModuleDefinitionUtils.findDefinition("c2", definition, true), child2);
		assertSame(ModuleDefinitionUtils.findDefinition("c3", definition, true), child3);
		assertNull(ModuleDefinitionUtils.findDefinition("c4", definition, true));
		
		assertSame(ModuleDefinitionUtils.findDefinition("c1-full", definition, true), child1);
		assertSame(ModuleDefinitionUtils.findDefinition("c1", definition, false), child1);
	}

}
