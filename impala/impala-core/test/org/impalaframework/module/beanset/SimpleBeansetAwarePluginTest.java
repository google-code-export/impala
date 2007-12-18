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

package org.impalaframework.module.beanset;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;

/**
 * @author Phil Zoio
 */
public class SimpleBeansetAwarePluginTest extends TestCase {

	public void testGetType() {
		SimpleBeansetModuleDefinition spec = new SimpleBeansetModuleDefinition("p1", new HashMap<String, Set<String>>());
		assertEquals(ModuleTypes.APPLICATION_WITH_BEANSETS, spec.getType());
	}
	
	@SuppressWarnings("unchecked")
	public void testConstructorsWithParent() {
		ModuleDefinition parent = new SimpleModuleDefinition("bean");
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
		map.put("key", Collections.EMPTY_SET);
		SimpleBeansetModuleDefinition spec = new SimpleBeansetModuleDefinition(parent, "p1", map);
		assertEquals(parent, spec.getParent());
		assertEquals("p1", spec.getName());
		assertEquals(Collections.EMPTY_SET, spec.getOverrides().get("key"));
		
		spec = new SimpleBeansetModuleDefinition(parent, "p1");
		assertEquals(parent, spec.getParent());
		assertEquals("p1", spec.getName());
		assertEquals(Collections.EMPTY_MAP, spec.getOverrides());
		
		spec = new SimpleBeansetModuleDefinition(parent, "p1", "key: value");
		assertEquals(parent, spec.getParent());
		assertEquals("p1", spec.getName());
		assertNotNull(spec.getOverrides().get("key"));
	}
	
	public void testEqualsObject() {
		Map<String, Set<String>> map1 = new HashMap<String, Set<String>>();
		SimpleBeansetModuleDefinition p1a = new SimpleBeansetModuleDefinition("p1", map1);
		Map<String, Set<String>> map2 = new HashMap<String, Set<String>>();
		SimpleBeansetModuleDefinition p1b = new SimpleBeansetModuleDefinition("p1", map2);
		assertEquals(p1a, p1b);

		SimpleBeansetModuleDefinition p2b = new SimpleBeansetModuleDefinition("p2", map2);
		assertFalse(p1b.equals(p2b));

		map1.put("bean1", Collections.singleton("context1-a.xml"));
		map1.put("bean2", Collections.singleton("context2-a.xml"));

		map2.put("bean1", Collections.singleton("context1-a.xml"));
		map2.put("bean2", Collections.singleton("context2-a.xml"));

		// these contain the same overrides, so use these
		p1a = new SimpleBeansetModuleDefinition("p1", map1);
		p1b = new SimpleBeansetModuleDefinition("p1", map2);
		assertEquals(p1a, p1b);

		// now change bean2 in map2
		map2.put("bean2", Collections.singleton("context2-b.xml"));
		p1b = new SimpleBeansetModuleDefinition("p1", map2);
		assertFalse(p1b.equals(p2b));

		map2.remove("bean2");
		p1b = new SimpleBeansetModuleDefinition("p1", map2);
		assertFalse(p1b.equals(p2b));
	}

	public void testEqualsMultipleObjects() {
		Set<String> set1 = new HashSet<String>();
		set1.add("set1");
		set1.add("set2");

		Set<String> set2 = new HashSet<String>();
		set2.add("set2");
		set2.add("set1");

		Set<String> set3 = new HashSet<String>();
		set1.add("set3");
		set1.add("set4");

		Set<String> set4 = new HashSet<String>();
		set2.add("set4");
		set2.add("set3");

		assertEquals(set1, set2);

		Map<String, Set<String>> map1 = new HashMap<String, Set<String>>();
		map1.put("key1", set1);
		map1.put("key2", set3);

		Map<String, Set<String>> map2 = new HashMap<String, Set<String>>();
		map2.put("key1", set2);
		map2.put("key2", set4);

		SimpleBeansetModuleDefinition p1a = new SimpleBeansetModuleDefinition("p1", map1);
		SimpleBeansetModuleDefinition p1b = new SimpleBeansetModuleDefinition("p1", map2);

		assertEquals(p1a, p1b);
	}

	public void testStringDefinition() {
		assertEquals(new SimpleBeansetModuleDefinition("p1", "key1: set1, set2; key2: set3, set4"),
				new SimpleBeansetModuleDefinition("p1", "key2: set4, set3; key1: set1, set2"));
		
		//no override specified for key 3, but result is still the same
		assertEquals(new SimpleBeansetModuleDefinition("p1", "key1: set1, set2; key2: set3, set4"),
				new SimpleBeansetModuleDefinition("p1", "key2: set4, set3; key1: set1, set2; key3: "));
	}

}
