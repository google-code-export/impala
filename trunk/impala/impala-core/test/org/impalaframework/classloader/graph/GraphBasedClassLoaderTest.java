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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.holder.graph.GraphClassLoaderFactory;
import org.impalaframework.module.holder.graph.GraphClassLoaderRegistry;

public class GraphBasedClassLoaderTest extends TestCase {

	private ModuleDefinition aDefinition;
	private ModuleDefinition cDefinition;
	private ModuleDefinition eDefinition;
	private DependencyManager dependencyManager;
	private GraphClassLoaderFactory factory;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		aDefinition = newDefinition(definitions, "a");
		newDefinition(definitions, "b", "a");
		cDefinition = newDefinition(definitions, "c");
		newDefinition(definitions, "d", "b");
		eDefinition = newDefinition(definitions, "e", "c,d");
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
		
		dependencyManager = new DependencyManager(definitions);
		factory = new GraphClassLoaderFactory();
		factory.setClassLoaderRegistry(new GraphClassLoaderRegistry());
		factory.setModuleLocationResolver(new TestClassResolver());
		
	}
	
	public void testResourceLoading() throws Exception {
		ClassLoader aClassLoader = factory.newClassLoader(dependencyManager, aDefinition);
		URL resource = aClassLoader.getResource("moduleA.txt");
		assertNotNull(resource);
		
		URL object = aClassLoader.getResource("java/lang/Object.class");
		assertNotNull(object);
	}

	public void testClassLoader() throws Exception {

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
		dependencyManager.removeModule("module-c");

		//notice that any of c's dependees no longer appear now
		printModuleDependees(dependencyManager, "module-a");
		
		//now add c, depending on a
		ModuleDefinition newC = new SimpleModuleDefinition(null, new String[] {"module-a"}, "module-c");
		
		//and e, with c as parent, and depending also on b
		new SimpleModuleDefinition(newC, new String[]{ "module-b" }, "module-e");
		
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
		ModuleDefinition definition = new SimpleModuleDefinition(null, split, "module-" + name);
		list.add(definition);
		return definition;
	}
	
	private ModuleDefinition newDefinition(List<ModuleDefinition> list, final String name) {
		ModuleDefinition definition = new SimpleModuleDefinition(null, new String[0], "module-" + name);
		list.add(definition);
		return definition;
	}
	
}

