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

public class DelegateClassLoaderFactoryTest extends TestCase {

	public void testClassLoader() throws Exception {
		
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		GraphModuleDefinition a = newDefinition(definitions, "a");
		GraphModuleDefinition b = newDefinition(definitions, "b", "a");
		newDefinition(definitions, "c");
		newDefinition(definitions, "d", "b");
		newDefinition(definitions, "e", "c,d");
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
		
		GraphClassLoader aClassLoader = factory.newClassLoader(dependencyManager, a);
		Object aImpl = aClassLoader.loadClass("AImpl").newInstance();
		
		GraphClassLoader bClassLoader = factory.newClassLoader(dependencyManager, b);
		Object bImpl = bClassLoader.loadClass("AImpl").newInstance();
		
		//show that AImpl loaded from b can be asssigned to to AImpl loaded from a
		assertTrue(aImpl.getClass().isAssignableFrom(bImpl.getClass()));
		
		try {
			aClassLoader.loadClass("duffClass");
			fail();
		} catch (ClassNotFoundException e) {
		}
		
	}

	private GraphModuleDefinition newDefinition(List<ModuleDefinition> list, final String name, final String dependencies) {
		final String[] split = dependencies.split(",");
		for (int i = 0; i < split.length; i++) {
			split[i] = "module-" + split[i];
		}
		final List<String> dependencyList = Arrays.asList(split);
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name, dependencyList);
		list.add(definition);
		return definition;
	}
	
	private GraphModuleDefinition newDefinition(List<ModuleDefinition> list, final String name) {
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name);
		list.add(definition);
		return definition;
	}
	
}