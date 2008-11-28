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

import junit.framework.TestCase;

import org.impalaframework.module.builder.InternalModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.graph.GraphClassLoaderFactory;
import org.impalaframework.module.holder.graph.GraphClassLoaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;

public class GraphClassLoaderTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testClassLoader() throws Exception {
		
		TypeReaderRegistry typeReaderRegistry = TypeReaderRegistryFactory.getTypeReaderRegistry();
		ModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		InternalModuleDefinitionSource source = new InternalModuleDefinitionSource(typeReaderRegistry, resolver, new String[]{"impala-core", "sample-module4", "sample-module6"});

		RootModuleDefinition rootDefinition = source.getModuleDefinition();
		
		DependencyManager dependencyManager = new DependencyManager(rootDefinition);
		GraphClassLoaderFactory factory = new GraphClassLoaderFactory();
		factory.setClassLoaderRegistry(new GraphClassLoaderRegistry());
		factory.setModuleLocationResolver(resolver);
		
		factory.newClassLoader(dependencyManager, rootDefinition);
		
	}
	
}

