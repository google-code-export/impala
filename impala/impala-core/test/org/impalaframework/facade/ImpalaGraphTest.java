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

package org.impalaframework.facade;

import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.classloader.graph.GraphClassLoader;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.builder.InternalModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.spring.SpringModuleUtils;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class ImpalaGraphTest extends TestCase implements ModuleDefinitionSource {

	private static final String plugin6 = "sample-module6";

	public void setUp() {
		Impala.clear();
		System.setProperty(FacadeConstants.FACADE_CLASS_NAME, GraphOperationFacade.class.getName());
		Impala.init();
	}

	public void tearDown() {
		try {
			Impala.clear();
		}
		catch (Exception e) {
		}
		System.clearProperty(FacadeConstants.FACADE_CLASS_NAME);
	}

	public void testGraph() throws Exception {
	
		Impala.init(this);
		ModuleStateHolder moduleStateHolder = Impala.getFacade().getModuleManagementFacade().getModuleStateHolder();
		Map<String, RuntimeModule> moduleContexts = moduleStateHolder.getRuntimeModules();
		System.out.println(moduleContexts);
		assertEquals(5, moduleContexts.size());
		assertNotNull(moduleContexts.get("impala-core"));
		assertNotNull(moduleContexts.get("sample-module2"));
		assertNotNull(moduleContexts.get("sample-module4"));
		assertNotNull(moduleContexts.get("sample-module5"));
		assertNotNull(moduleContexts.get("sample-module6"));
		
		ConfigurableApplicationContext applicationContext = SpringModuleUtils.getModuleSpringContext(Impala.getRuntimeModule(plugin6));
		assertNotNull(applicationContext);
		ClassLoader classLoader = applicationContext.getClassLoader();
		assertTrue(classLoader instanceof GraphClassLoader);
	}

	public RootModuleDefinition getModuleDefinition() {
		return new InternalModuleDefinitionSource(TypeReaderRegistryFactory.getTypeReaderRegistry(), 
				Impala.getFacade().getModuleManagementFacade().getModuleLocationResolver(), 
				new String[] { "impala-core", "sample-module4",	"sample-module5", "sample-module6" }).getModuleDefinition();
	}
	
}
