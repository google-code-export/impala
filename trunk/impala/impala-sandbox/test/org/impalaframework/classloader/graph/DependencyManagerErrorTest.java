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

import static org.impalaframework.classloader.graph.GraphTestUtils.assertModules;
import static org.impalaframework.classloader.graph.GraphTestUtils.newDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphRootModuleDefinition;


public class DependencyManagerErrorTest extends TestCase {
	
	private SimpleGraphRootModuleDefinition rootDefinition;
	private DependencyManager manager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		rootDefinition = definitionSet1();
		manager = new DependencyManager(rootDefinition);
	}
	
	public void testOK() throws Exception {
		Collection<ModuleDefinition> allModules = manager.getAllModules();
		assertModules("b,a,root,f,e", allModules);
		
		//FIXME add tests for various dependency manager corner cases
	}

	private SimpleGraphRootModuleDefinition definitionSet1() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		//a has no parent or dependencies
		ModuleDefinition a = newDefinition(definitions, null, "a", null);
		
		//b depends on d but has no parent
		ModuleDefinition b = newDefinition(definitions, null, "b", null);
		
		//root has siblings a, b and depends on a
		SimpleGraphRootModuleDefinition root = new SimpleGraphRootModuleDefinition("root", 
				Collections.singletonList("root.xml"), 
				Arrays.asList("a"), 
				Arrays.asList(a, b));
		
		//e has parent root, and depends on b
		newDefinition(definitions, root, "e", "b");
		
		//f has parent root, but no dependencies
		newDefinition(definitions, root, "f", null);
	
		return root;
	}
	
}
