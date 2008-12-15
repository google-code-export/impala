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

/**
 * @author Phil Zoio
 */
public class SimpleRootModuleDefinitionTest extends TestCase {

	private String rootModuleName = "project1";
	
	public void testParent() {
		SimpleRootModuleDefinition definition = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"});
		assertEquals("project1", definition.getName());
		assertNull(definition.getParentDefinition());
		
		SimpleModuleDefinition child1 = new SimpleModuleDefinition(definition, "c1");
		SimpleModuleDefinition child2 = new SimpleModuleDefinition(definition, "c2");
		assertTrue(definition.hasDefinition("c1"));
		assertTrue(definition.hasDefinition("c2"));
		assertEquals(2, definition.getChildDefinitions().size());
		assertEquals(2, definition.getChildDefinitions().size());
		
		assertSame(child1, definition.getModule("c1"));
		assertSame(child2, definition.getModule("c2"));
	}
	
	public void testDefaultContextLocations() {
		checkContext(new SimpleRootModuleDefinition(rootModuleName, new String[0]));
	}

	private void checkContext(SimpleRootModuleDefinition definition) {
		assertEquals("project1-context.xml", definition.getContextLocations().get(0));
	}
	
	public void testEquals() {
		SimpleRootModuleDefinition spec1 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"});
		SimpleRootModuleDefinition spec2 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"});
		assertEquals(spec1, spec2);
		SimpleRootModuleDefinition spec3 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1"});
		SimpleRootModuleDefinition spec4 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p3"});
		assertFalse(spec1.equals(spec3));
		assertFalse(spec1.equals(spec4));
		
		SimpleRootModuleDefinition spec5 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"}, new String[]{"dep1", "dep2"}, null);
		SimpleRootModuleDefinition spec6 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"}, new String[]{"dep1", "dep2"}, null);
		assertEquals(spec5, spec6);
		SimpleRootModuleDefinition spec7 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1"}, new String[]{"dep1"}, null);
		SimpleRootModuleDefinition spec8 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p3"}, new String[]{"dep2"}, null);
		assertFalse(spec5.equals(spec7));
		assertFalse(spec5.equals(spec8));
	}
	
	public void testContains() {
		SimpleRootModuleDefinition spec1 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"});
		SimpleRootModuleDefinition spec2 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"});
		assertTrue(spec1.containsAll(spec2));
		assertTrue(spec2.containsAll(spec2));
		SimpleRootModuleDefinition spec3 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1"});
		SimpleRootModuleDefinition spec4 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p3"});
		assertTrue(spec1.containsAll(spec3));
		assertFalse(spec3.containsAll(spec1));
		assertFalse(spec1.containsAll(spec4));
	}
	
	public void testAddLocations() {
		SimpleRootModuleDefinition spec1 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1"});
		SimpleRootModuleDefinition spec2 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1", "p2"});
		spec1.addContextLocations(spec2);
		assertEquals(spec1.getContextLocations(), spec2.getContextLocations());
	}
	
	public void testAddSibling() {
		SimpleRootModuleDefinition spec1 = new SimpleRootModuleDefinition(rootModuleName, new String[]{"p1"});
		spec1.addSibling(new SimpleModuleDefinition("sibling1"));
		assertNotNull(spec1.findChildDefinition("sibling1", true));
		assertNotNull(spec1.getSiblingModule("sibling1"));
	}


}
