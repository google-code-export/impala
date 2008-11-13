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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class GraphBasedClassLoaderTest extends TestCase {

	public void testClassLoader() throws Exception {
		DependencyRegistry registry = new DependencyRegistry(new GraphClassLoaderFactory(new TestClassResolver()));
		
		List<ModuleDefinition> list = new ArrayList<ModuleDefinition>();
		
		newDefinition(list, "a");
		newDefinition(list, "b", "a");
		newDefinition(list, "c");
		newDefinition(list, "d", "b");
		newDefinition(list, "e", "c,d");
		newDefinition(list, "f", "b,e");
		newDefinition(list, "g", "c,d,f");	
		
		/*
a
b depends on a
c
d depends on b
e depends on c, d
f depends on b, e
g on c, d, f
		 */
		
		registry.buildVertexMap(list);
		
		GraphBasedClassLoader eClassLoader = new GraphBasedClassLoader(registry, "module-e");
		System.out.println(eClassLoader.toString());
		
		System.out.println(eClassLoader.loadClass("E"));
		System.out.println(eClassLoader.loadClass("EImpl"));
		System.out.println(eClassLoader.loadClass("C"));
		System.out.println(eClassLoader.loadClass("B"));
		System.out.println(eClassLoader.loadClass("A"));

		failToLoad(eClassLoader, "F");
		
		printModuleDependees(registry, "module-a");
		printModuleDependees(registry, "module-b");
		printModuleDependees(registry, "module-c");
		printModuleDependees(registry, "module-d");
		printModuleDependees(registry, "module-e");
		printModuleDependees(registry, "module-f");
		printModuleDependees(registry, "module-g");
		
		System.out.println("------------------ Removing vertices for c --------------------");
		registry.remove("module-c");

		try {
		new GraphBasedClassLoader(registry, "module-c");
		fail(); }
		catch (IllegalStateException e) {}
		
		//notice that any of c's dependees no longer appear now
		printModuleDependees(registry, "module-a");
		
		//now add c, depending on a
		GraphModuleDefinition newC = new SimpleGraphModuleDefinition("module-c", Arrays.asList("module-a"));
		
		//and e, with c as parent, and depending also on b
		new SimpleGraphModuleDefinition(newC, "module-e", Arrays.asList("module-b"));
		
		registry.addModule("module-a", newC);
		
		//we should see c and e in the list of dependencies
		printModuleDependees(registry, "module-a");
		
		//now we load class C
		final GraphBasedClassLoader cClassLoader = new GraphBasedClassLoader(registry, "module-c");
		System.out.println(cClassLoader.loadClass("C"));
		System.out.println(cClassLoader);
		
	}

	private void failToLoad(GraphBasedClassLoader classLoader,
			final String className) {
		try {
			classLoader.loadClass(className);
			fail();
		} catch (ClassNotFoundException e) {
		}
	}

	private void printModuleDependees(DependencyRegistry registry,
			final String moduleName) {
		System.out.println("--------------- Module dependees: " + moduleName);
		final List<ModuleDefinition> dependees = registry.getDependees(moduleName);
		for (ModuleDefinition moduleDefinition : dependees) {
			System.out.println(moduleDefinition.getName());
		}
		System.out.println("---------------------------------------------");
	}

	private void newDefinition(List<ModuleDefinition> list, final String name, final String dependencies) {
		final String[] split = dependencies.split(",");
		for (int i = 0; i < split.length; i++) {
			split[i] = "module-" + split[i];
		}
		final List<String> dependencyList = Arrays.asList(split);
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name, dependencyList);
		list.add(definition);
	}
	
	private void newDefinition(List<ModuleDefinition> list, final String name) {
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name);
		list.add(definition);
	}
	
}

class TestClassResolver implements ModuleLocationResolver {

	private String rootLocation = "../impala-core/files/impala-classloader";
	
	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		File root = rootFileLocation();
		File moduleDirectory = new File(root, moduleName);
		File classDirectory = new File(moduleDirectory, "bin");
		final Resource resource = new FileSystemResource(classDirectory);
		
		Assert.isTrue(resource.exists());
		
		return Collections.singletonList(resource);
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		throw new UnsupportedOperationException();
	}

	public Resource getRootDirectory() {
		File file = rootFileLocation();
		return new FileSystemResource(file);
	}

	private File rootFileLocation() {
		File file = new File(rootLocation);
		return file;
	}
	
}
