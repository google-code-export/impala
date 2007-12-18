/*
 * Copyright 2007 the original author or authors.
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

import org.impalaframework.module.definition.SimpleModuleDefinition;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class SimpleModuleDefinitionTest extends TestCase {

	public void testSimplePluginSpecStringStringArray() {

		SimpleModuleDefinition spec = new SimpleModuleDefinition("p1");

		assertEquals(1, spec.getContextLocations().size());
		assertEquals("p1-context.xml", spec.getContextLocations().get(0));
		SimpleModuleDefinition child1 = new SimpleModuleDefinition(spec, "c1");
		SimpleModuleDefinition child2 = new SimpleModuleDefinition(spec, "c2");
		assertTrue(spec.hasPlugin("c1"));
		assertTrue(spec.hasPlugin("c2"));
		assertEquals(2, spec.getPlugins().size());
		assertEquals(2, spec.getPlugins().size());
		
		assertSame(child1, spec.getPlugin("c1"));
		assertSame(child2, spec.getPlugin("c2"));

	}

}
