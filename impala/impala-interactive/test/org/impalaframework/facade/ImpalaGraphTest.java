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

import junit.framework.TestCase;

import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;

public class ImpalaGraphTest extends TestCase implements ModuleDefinitionSource {

	/*
	private static final String plugin1 = "sample-module1";
	private static final String plugin2 = "sample-module2";
	private static final String plugin5 = "sample-module5";
	private static final String plugin6 = "sample-module6";
	*/

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
		
		/*
		Cannot enable this until we have implemented graph based sticky modification extractor
		Impala.init(this);
		ModuleStateHolder moduleStateHolder = Impala.getFacade().getModuleManagementFacade().getModuleStateHolder();
		System.out.println(moduleStateHolder.getModuleContexts());
		
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) Impala.getModule(plugin6);
		assertNotNull(applicationContext);
		ClassLoader classLoader = applicationContext.getClassLoader();
		System.out.println(classLoader);
		*/
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("sample-module6").getModuleDefinition();
	}
	
}
