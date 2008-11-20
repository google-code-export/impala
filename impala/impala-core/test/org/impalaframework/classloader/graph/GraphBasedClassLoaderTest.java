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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphModuleDefinition;
import org.impalaframework.module.holder.graph.GraphClassLoaderFactory;
import org.impalaframework.module.holder.graph.GraphClassLoaderRegistry;

public class GraphBasedClassLoaderTest extends TestCase {

	public void testClassLoader() throws Exception {
		
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		newDefinition(definitions, "a");
		newDefinition(definitions, "b", "a");
		ModuleDefinition cDefinition = newDefinition(definitions, "c");
		newDefinition(definitions, "d", "b");
		ModuleDefinition eDefinition = newDefinition(definitions, "e", "c,d");
		newDefinition(definitions, "f", "b,e");
		newDefinition(definitions, "g", "c,d,f");	
		
		/*
a
b depends on a
c
d depends on b
e depends on c, d
f depends on b, e
g on c, d, f
		 */
		DependencyManager dependencyManager = new DependencyManager(definitions);
		GraphClassLoaderFactory factory = new GraphClassLoaderFactory();
		factory.setClassLoaderRegistry(new GraphClassLoaderRegistry());
		factory.setModuleLocationResolver(new TestClassResolver());
		
		ClassLoader eClassLoader = factory.newClassLoader(dependencyManager, eDefinition);
		System.out.println(eClassLoader.toString());
		
		System.out.println(eClassLoader.loadClass("E"));
		System.out.println(eClassLoader.loadClass("EImpl"));
		System.out.println(eClassLoader.loadClass("C"));
		System.out.println(eClassLoader.loadClass("B"));
		System.out.println(eClassLoader.loadClass("A"));
		
		Object cfromE = eClassLoader.loadClass("CImpl").newInstance();
		
		ClassLoader cClassLoader = factory.newClassLoader(dependencyManager, cDefinition);
		System.out.println(cClassLoader.toString());
		
		Object cfromC = cClassLoader.loadClass("CImpl").newInstance();
		
		assertTrue(cfromC.getClass().isAssignableFrom(cfromE.getClass()));
		
		System.out.println("From C class loader: " + cfromC.getClass().getClassLoader());
		System.out.println("From E class loader: " + cfromE.getClass().getClassLoader());

		failToLoad(eClassLoader, "F");
		
		printModuleDependees(dependencyManager, "module-a");
		printModuleDependees(dependencyManager, "module-b");
		printModuleDependees(dependencyManager, "module-c");
		printModuleDependees(dependencyManager, "module-d");
		printModuleDependees(dependencyManager, "module-e");
		printModuleDependees(dependencyManager, "module-f");
		printModuleDependees(dependencyManager, "module-g");
		
		System.out.println("------------------ Removing vertices for c --------------------");
		dependencyManager.remove("module-c");

		//notice that any of c's dependees no longer appear now
		printModuleDependees(dependencyManager, "module-a");
		
		//now add c, depending on a
		GraphModuleDefinition newC = new SimpleGraphModuleDefinition("module-c", Arrays.asList("module-a"));
		
		//and e, with c as parent, and depending also on b
		new SimpleGraphModuleDefinition(newC, "module-e", Arrays.asList("module-b"));
		
		dependencyManager.addModule("module-a", newC);
		
		//we should see c and e in the list of dependencies
		printModuleDependees(dependencyManager, "module-a");
		
	}

	private void failToLoad(ClassLoader classLoader,
			final String className) {
		try {
			classLoader.loadClass(className);
			fail();
		} catch (ClassNotFoundException e) {
		}
	}

	private void printModuleDependees(DependencyManager dependencyManager,
			final String moduleName) {
		System.out.println("--------------- Module dependees: " + moduleName);
		final List<ModuleDefinition> dependees = dependencyManager.getOrderedModuleDependees(moduleName);
		for (ModuleDefinition moduleDefinition : dependees) {
			System.out.println(moduleDefinition.getName());
		}
		System.out.println("---------------------------------------------");
	}

	private ModuleDefinition newDefinition(List<ModuleDefinition> list, final String name, final String dependencies) {
		final String[] split = dependencies.split(",");
		for (int i = 0; i < split.length; i++) {
			split[i] = "module-" + split[i];
		}
		final List<String> dependencyList = Arrays.asList(split);
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name, dependencyList);
		list.add(definition);
		return definition;
	}
	
	private ModuleDefinition newDefinition(List<ModuleDefinition> list, final String name) {
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name);
		list.add(definition);
		return definition;
	}
	
}

