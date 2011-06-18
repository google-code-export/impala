/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.module.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.util.XMLDomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ApplicationWithBeansetsModuleTypeReaderTest extends TestCase {
	private ApplicationWithBeansetsModuleTypeReader reader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		reader = new ApplicationWithBeansetsModuleTypeReader();
	}

	public void testReadModuleDefinition() {
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", new Properties());
		SimpleBeansetModuleDefinition moduleDefinition = (SimpleBeansetModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals("APPLICATION_WITH_BEANSETS", moduleDefinition.getType());
	}
	
	public void testReadModuleDefinitionLocations() {
		Properties properties = new Properties();
		properties.put(ModuleElementNames.CONFIG_LOCATIONS_ELEMENT, "loc1, loc2,loc3");
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", properties);
		SimpleBeansetModuleDefinition moduleDefinition = (SimpleBeansetModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals("APPLICATION_WITH_BEANSETS", moduleDefinition.getType());
		assertEquals(Arrays.asList(new String[]{ "loc1", "loc2", "loc3"}), moduleDefinition.getConfigLocations());
	}
	
	public void testWithOverrides() {
		Properties properties = new Properties();
		properties.setProperty(ApplicationWithBeansetsModuleTypeReader.OVERRIDES_ELEMENT, "beanset: all;");
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", properties);
		SimpleBeansetModuleDefinition moduleDefinition = (SimpleBeansetModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals("APPLICATION_WITH_BEANSETS", moduleDefinition.getType());
		assertEquals(Collections.singletonMap("beanset", Collections.singleton("all")), moduleDefinition.getOverrides());
	}
	
	public void testReadModuleDefinitionProperties() throws Exception {
	    Document document = XMLDomUtils.newDocument();
	    Element root = document.createElement("root");
	    document.appendChild(root);
	    
		Element locations = document.createElement("overrides");
	    locations.setTextContent("overrides1");
	    root.appendChild(locations);
	    
		Properties properties = new Properties();
		reader.readModuleDefinitionProperties(properties, "mymodule", root);
		System.out.println(properties);
		assertEquals("overrides1", properties.get("overrides"));
	}
}
