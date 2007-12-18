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

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class SimpleRootModuleDefinitionTest extends TestCase {

	public void testParent() {
		SimpleRootModuleDefinition spec = new SimpleRootModuleDefinition(new String[]{"p1", "p2"});
		assertEquals(RootModuleDefinition.NAME, spec.getName());
		assertNull(spec.getParent());
		
		SimpleModuleDefinition child1 = new SimpleModuleDefinition(spec, "c1");
		SimpleModuleDefinition child2 = new SimpleModuleDefinition(spec, "c2");
		assertTrue(spec.hasPlugin("c1"));
		assertTrue(spec.hasPlugin("c2"));
		assertEquals(2, spec.getPlugins().size());
		assertEquals(2, spec.getPlugins().size());
		
		assertSame(child1, spec.getPlugin("c1"));
		assertSame(child2, spec.getPlugin("c2"));
	}
	
	public void testEquals() {
		SimpleRootModuleDefinition spec1 = new SimpleRootModuleDefinition(new String[]{"p1", "p2"});
		SimpleRootModuleDefinition spec2 = new SimpleRootModuleDefinition(new String[]{"p1", "p2"});
		assertEquals(spec1, spec2);
		SimpleRootModuleDefinition spec3 = new SimpleRootModuleDefinition(new String[]{"p1"});
		SimpleRootModuleDefinition spec4 = new SimpleRootModuleDefinition(new String[]{"p1", "p3"});
		assertFalse(spec1.equals(spec3));
		assertFalse(spec1.equals(spec4));
	}
	
	public void testContains() {
		SimpleRootModuleDefinition spec1 = new SimpleRootModuleDefinition(new String[]{"p1", "p2"});
		SimpleRootModuleDefinition spec2 = new SimpleRootModuleDefinition(new String[]{"p1", "p2"});
		assertTrue(spec1.containsAll(spec2));
		assertTrue(spec2.containsAll(spec2));
		SimpleRootModuleDefinition spec3 = new SimpleRootModuleDefinition(new String[]{"p1"});
		SimpleRootModuleDefinition spec4 = new SimpleRootModuleDefinition(new String[]{"p1", "p3"});
		assertTrue(spec1.containsAll(spec3));
		assertFalse(spec3.containsAll(spec1));
		assertFalse(spec1.containsAll(spec4));
	}
	
	public void testAddLocations() {
		SimpleRootModuleDefinition spec1 = new SimpleRootModuleDefinition(new String[]{"p1"});
		SimpleRootModuleDefinition spec2 = new SimpleRootModuleDefinition(new String[]{"p1", "p2"});
		spec1.addContextLocations(spec2);
		assertEquals(spec1.getContextLocations(), spec2.getContextLocations());
	}


}
