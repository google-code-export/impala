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

import junit.framework.TestCase;

import org.impalaframework.module.builder.XmlModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.core.io.ClassPathResource;

public class XmlModuleDefinitionSourceTest extends TestCase {

	private static final String plugin1 = "sample-module1";

	private static final String plugin2 = "sample-module2";

	private static final String plugin3 = "sample-module3";

	private static final String plugin4 = "sample-module4";
	
	private String rootModuleName = "project1";

	private XmlModuleDefinitionSource builder;
	
	@Override
	protected void setUp() throws Exception {
		builder = new XmlModuleDefinitionSource();
	}
	
	public final void testGetParentOnlyDefinition() {
		builder.setResource(new ClassPathResource("xmlspec/parent-only-spec.xml"));
		RootModuleDefinition actual = builder.getModuleDefinition();
		assertEquals(0, actual.getChildDefinitions().size());

		RootModuleDefinition expected = new SimpleRootModuleDefinition(rootModuleName, new String[] { "parentTestContext.xml", "extra-context.xml" });
		assertEquals(expected, actual);
	}
	
	public final void testGetParentDefinition() {
		builder.setResource(new ClassPathResource("xmlspec/moduledefinition.xml"));
		RootModuleDefinition actual = builder.getModuleDefinition();
		assertEquals(3, actual.getChildDefinitions().size());

		RootModuleDefinition expected = new SimpleRootModuleDefinition(rootModuleName, new String[] { "parentTestContext.xml", "extra-context.xml" });
		assertEquals(expected, actual);
		
		ModuleDefinition spec1 = new SimpleModuleDefinition(expected, plugin1);
		ModuleDefinition spec2 = new SimpleModuleDefinition(expected, plugin2);
		ModuleDefinition spec3 = new SimpleModuleDefinition(spec2, plugin3);
		ModuleDefinition spec4 = new SimpleBeansetModuleDefinition(expected, plugin4, "alternative: myImports");
		
		assertEquals(spec1, actual.findChildDefinition(plugin1, true));
		assertEquals(spec2, actual.findChildDefinition(plugin2, true));
		assertEquals(spec3, actual.findChildDefinition(plugin3, true));
		assertEquals(spec4, actual.findChildDefinition(plugin4, true));
	}

}
