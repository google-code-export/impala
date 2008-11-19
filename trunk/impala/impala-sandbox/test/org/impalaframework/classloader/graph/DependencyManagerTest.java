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

package org.impalaframework.classloader.graph;

import static org.impalaframework.classloader.graph.GraphTestUtils.assertContainsOnly;
import static org.impalaframework.classloader.graph.GraphTestUtils.expectedAsList;
import static org.impalaframework.classloader.graph.GraphTestUtils.findDefintion;
import static org.impalaframework.classloader.graph.GraphTestUtils.newDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.graph.SimpleGraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphRootModuleDefinition;


public class DependencyManagerTest extends TestCase {
	
	private SimpleGraphRootModuleDefinition rootDefinition;
	private DependencyManager registry;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		rootDefinition = definitionSet1();
		registry = new DependencyManager(rootDefinition);
	}
	
	public void testGetDirectDependees() throws Exception {
		assertDirectDependee("a", "root");
		assertDirectDependee("b", "root,e");
		assertDirectDependee("c", "f");
		assertDirectDependee("d", "e,b");
		assertDirectDependee("root", "e");
		assertDirectDependee("e", "f,g");
		assertDirectDependee("f", "g");
		assertDirectDependee("g", null);
	}
	
	public void testGetDependencies() throws Exception {
		assertDependencies("a", "a");
		assertDependencies("b", "d,b");
		assertDependencies("c", "c");
		assertDependencies("d", "d");
		assertDependencies("root", "a,d,b,root");
		assertDependencies("e", "a,d,b,root,e");
		assertDependencies("f", "a,d,c,b,root,e,f");
		assertDependencies("g", "a,d,c,b,root,e,f,g");
	}
	
	public void testGetDependees() throws Exception {
		assertDependees("a", "a,root,e,f,g");
		assertDependees("b", "b,root,e,f,g");
		assertDependees("c", "c,f,g");
		assertDependees("d", "d,b,root,e,f,g");
		assertDependees("root", "root,e,f,g");
		assertDependees("e", "e,f,g");
		assertDependees("f", "f,g");
		assertDependees("g", "g");
	}
	
	public void testAllModules() throws Exception {
		Collection<ModuleDefinition> allModules = registry.getAllModules();
		assertModules("d,a,c,b,root,e,f,g", allModules);
	}
	
	public void testAddH() throws Exception {
		registry.addModule("root", new SimpleGraphModuleDefinition("h", Arrays.asList("a")));
		Collection<ModuleDefinition> allModules = registry.getAllModules();
		assertModules("d,a,c,b,root,h,e,f,g", allModules);
		assertDependees("root", "root,h,e,f,g");
		assertDependencies("g", "a,d,c,b,root,e,f,g");
		assertDependencies("h", "a,d,b,root,h");
	}
	
	public void testAddI() throws Exception {
		//add i with parent c, and depending on g
		registry.addModule("c", new SimpleGraphModuleDefinition("i", Arrays.asList("c", "g")));
		Collection<ModuleDefinition> allModules = registry.getAllModules();
		assertModules("d,a,c,b,root,e,f,g,i", allModules);
		assertDependencies("i", "c,a,d,b,root,e,f,g,i");
		assertDependees("root", "root,e,f,g,i");
		assertDependees("a", "a,root,e,f,g,i");
		assertDependencies("g", "a,d,c,b,root,e,f,g");
	}

	private SimpleGraphRootModuleDefinition definitionSet1() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		//a has no parent or dependencies
		ModuleDefinition a = newDefinition(definitions, null, "a", null);
		
		//b depends on d but has no parent
		ModuleDefinition b = newDefinition(definitions, null, "b", "d");
		
		//c has no parent or dependencies
		ModuleDefinition c = newDefinition(definitions, null, "c", null);
		
		//d has no parent or dependencies
		ModuleDefinition d = newDefinition(definitions, null, "d", null);
		
		//root has siblings a to d, and depends on a and b
		SimpleGraphRootModuleDefinition root = new SimpleGraphRootModuleDefinition("root", 
				Collections.singletonList("root.xml"), 
				Arrays.asList("a", "b"), 
				Arrays.asList(a, b, c, d));
		
		//e has parent root, and depends on b an d
		ModuleDefinition e = newDefinition(definitions, root, "e", "b,d");
		
		//has parent e, and depends on c
		newDefinition(definitions, e, "f", "c");

		//has parent e, depends on f
		newDefinition(definitions, e, "g", "f");
		return root;
	}	

	private void assertDependencies(String moduleName, String expected) {
		List<ModuleDefinition> orderedModuleDependencies = registry.getOrderedModuleDependencies(moduleName);
		assertModules(expected, orderedModuleDependencies);
	}

	private void assertDependees(String moduleName, String expected) {
		List<ModuleDefinition> orderedModuleDependencies = registry.getOrderedModuleDependees(moduleName);
		assertModules(expected, orderedModuleDependencies);
	}
	
	private void assertDirectDependee(
			String module,
			String expected) {
		
		ModuleDefinition definition = findDefintion(rootDefinition,	module);
		Collection<ModuleDefinition> directDependees = registry.getDirectDependees(definition.getName());
		System.out.println(directDependees);
		assertContainsOnly(directDependees, expected);
	}
	
	private void assertModules(String expected,	Collection<ModuleDefinition> moduleDefinitions) {
		List<String> actualNames = ModuleDefinitionUtils.getModuleNamesFromCollection(moduleDefinitions);
		List<String> expectedNames = expectedAsList(expected);
		assertEquals(expectedNames, actualNames);
	}

	
}
