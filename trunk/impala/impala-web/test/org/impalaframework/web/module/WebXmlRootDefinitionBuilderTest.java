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

package org.impalaframework.web.module;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.web.module.ServletModuleDefinition;
import org.impalaframework.web.module.WebPlaceholderModuleDefinition;
import org.impalaframework.web.module.WebRootModuleDefinition;
import org.impalaframework.web.module.WebXmlRootDefinitionBuilder;
import org.impalaframework.web.type.WebTypeReaderRegistryFactory;
import org.springframework.core.io.ClassPathResource;

public class WebXmlRootDefinitionBuilderTest extends TestCase {

	private String projectNames = "project1";
	private WebXmlRootDefinitionBuilder builder;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		builder = new WebXmlRootDefinitionBuilder(WebTypeReaderRegistryFactory.getTypeReaderRegistry());
	}
	
	public final void testCreateModuleDefinition() {
		builder.setResource(new ClassPathResource("xmlspec/webspec.xml"));
		RootModuleDefinition actual = builder.getModuleDefinition();
		assertEquals(5, actual.getChildDefinitions().size());
		
		RootModuleDefinition expected = new SimpleRootModuleDefinition(projectNames, new String[] { "parentTestContext.xml" });
		assertEquals(expected, actual);
		
		ModuleDefinition definition1 = new SimpleModuleDefinition(expected, "plugin1");
		ModuleDefinition definition2 = new SimpleModuleDefinition(expected, "plugin2", new String[]{"location1","location2"});
		ModuleDefinition definition3 = new WebRootModuleDefinition(expected, "servlet1", new String[]{"location1", "location2"});
		ModuleDefinition definition4 = new ServletModuleDefinition(expected, "servlet2", new String[]{"location3", "location4" });
		ModuleDefinition definition5 = new WebPlaceholderModuleDefinition(expected, "servlet3");

		assertEquals(definition1, actual.findChildDefinition("plugin1", true));
		assertEquals(definition2, actual.findChildDefinition("plugin2", true));
		assertEquals(definition3, actual.findChildDefinition("servlet1", true));
		assertEquals(definition4, actual.findChildDefinition("servlet2", true));
		assertEquals(definition5, actual.findChildDefinition("servlet3", true));
	}

}
