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

package org.impalaframework.module.builder;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleElementNames;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;

public class InternalModuleBuilderTest extends TestCase {

	private Map<String, Set<String>> children;
	private Map<String, Properties> moduleProperties;
	private String rootModuleName;
	private TypeReaderRegistry typeReaderRegistry;

	protected void setUp() throws Exception {
		super.setUp();
		this.typeReaderRegistry = TypeReaderRegistryFactory.getTypeReaderRegistry();
		StandaloneModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		String[] moduleNames = new String[] { "sample-module4" };
		InternalModuleDefinitionSource moduleDefinitionSource = new InternalModuleDefinitionSource(
				typeReaderRegistry, resolver, moduleNames, true);
		moduleDefinitionSource.inspectModules();
		children = moduleDefinitionSource.getChildren();
		moduleProperties = moduleDefinitionSource.getModuleProperties();
		rootModuleName = moduleDefinitionSource.getRootModuleName();
	}

	public void testGetModuleDefinition() {
		InternalModuleBuilder builder = new InternalModuleBuilder(typeReaderRegistry, rootModuleName, moduleProperties, children);
		RootModuleDefinition definition = builder.getModuleDefinition();
		System.out.println(definition);
		
		RootModuleDefinition root = new SimpleRootModuleDefinition("impala-core", "parentTestContext.xml");
		SimpleModuleDefinition sample2 = new SimpleModuleDefinition(root, "sample-module2");
		new SimpleBeansetModuleDefinition(sample2, "sample-module4");
		
		assertEquals(root, definition);
	}
	
	public void testGetType() throws Exception {
		InternalModuleBuilder builder = new InternalModuleBuilder();
		Properties properties = new Properties();
		assertEquals(ModuleTypes.APPLICATION, builder.getType(properties));
		
		properties.put(ModuleElementNames.TYPE_ELEMENT, "atype");
		assertEquals("atype", builder.getType(properties));
	}

}
