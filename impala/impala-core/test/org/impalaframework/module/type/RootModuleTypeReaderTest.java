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

package org.impalaframework.module.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.util.XmlDomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RootModuleTypeReaderTest extends TestCase {

	private RootModuleTypeReader reader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		reader = new RootModuleTypeReader();
	}

	public void testReadModuleDefinitionDefaults() {
		Properties properties = new Properties();
		ModuleDefinition moduleDefinition = reader.readModuleDefinition(null, "rootModule", properties);
		SimpleRootModuleDefinition definition = (SimpleRootModuleDefinition) moduleDefinition;
		assertEquals(Collections.singletonList("rootModule-context.xml"), definition.getContextLocations());
	}

	public void testReadModuleDefinition() {
		Properties properties = new Properties();
		properties.setProperty(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, "loc1,loc2");
		properties.put(ModuleElementNames.DEPENDENCIES_ELEMENT, "module1,module2, module3 , module4 module5");
	
		ModuleDefinition moduleDefinition = reader.readModuleDefinition(null, "rootModule", properties);
		SimpleRootModuleDefinition definition = (SimpleRootModuleDefinition) moduleDefinition;
		assertEquals(Arrays.asList(new String[]{"loc1", "loc2"}), definition.getContextLocations());
		assertEquals(Arrays.asList(new String[]{ "module1", "module2", "module3", "module4", "module5"}), moduleDefinition.getDependentModuleNames());
	}
	
	public void testReadModuleDefinitionProperties() throws Exception {
	    Document document = XmlDomUtils.newDocument();
	    Element root = document.createElement("root");
	    document.appendChild(root);
	    
		Element locations = document.createElement("context-locations");
	    root.appendChild(locations);
	    
	    Element location1 = document.createElement("context-location");
	    location1.setTextContent("location1");
	    locations.appendChild(location1);
	    
	    Element location2 = document.createElement("context-location");
	    location2.setTextContent("location2");
	    locations.appendChild(location2);
	    
	    Element dependsOn = document.createElement("depends-on");
	    dependsOn.setTextContent("module1,module2, module3 , module4 module5");
	    root.appendChild(dependsOn);	    
	    
		Properties properties = new Properties();
		reader.readModuleDefinitionProperties(properties, "mymodule", root);
		System.out.println(properties);
		assertEquals("location1,location2", properties.get("context-locations"));
		assertEquals("module1,module2,module3,module4,module5", properties.get("depends-on"));
		
		ModuleDefinition moduleDefinition = reader.readModuleDefinition(null, "mymodule", root);
		assertEquals(Arrays.asList(new String[]{ "location1", "location2"}), moduleDefinition.getContextLocations());
		assertEquals(Arrays.asList(new String[]{ "module1", "module2", "module3", "module4", "module5"}), moduleDefinition.getDependentModuleNames());

	}

}
