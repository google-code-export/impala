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

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.util.XMLDomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ApplicationModuleTypeReaderTest extends TestCase {

    private ApplicationModuleTypeReader reader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        reader = new ApplicationModuleTypeReader();
    }

    public void testReadModuleDefinition() {
        ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", new Properties());
        SimpleModuleDefinition moduleDefinition = (SimpleModuleDefinition) definition;
        assertEquals("mymodule", moduleDefinition.getName());
        assertEquals(ModuleTypes.APPLICATION, moduleDefinition.getType());
        assertEquals("spring", moduleDefinition.getRuntimeFramework());
    }
    
    public void testReadModuleDefinitionLocations() {
        Properties properties = new Properties();
        properties.put(ModuleElementNames.CONFIG_LOCATIONS_ELEMENT, "loc1, loc2,loc3");
        properties.put(ModuleElementNames.DEPENDENCIES_ELEMENT, "module1,module2, module3 , module4 module5");
        properties.put("prop1", "value1");
        properties.put("prop2", "value2");
        properties.put("runtime", "myruntime");
        ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", properties);
        SimpleModuleDefinition moduleDefinition = (SimpleModuleDefinition) definition;
        assertEquals("mymodule", moduleDefinition.getName());
        assertEquals(ModuleTypes.APPLICATION, moduleDefinition.getType());
        assertEquals(Arrays.asList(new String[]{ "loc1", "loc2", "loc3"}), moduleDefinition.getConfigLocations());
        assertEquals(Arrays.asList(new String[]{ "module1", "module2", "module3", "module4", "module5"}), moduleDefinition.getDependentModuleNames());
        
        Map<String,String> expectedAttributes = new HashMap<String,String>();
        expectedAttributes.put("prop1", "value1");
        expectedAttributes.put("prop2", "value2");
        assertEquals(expectedAttributes, moduleDefinition.getAttributes());
        assertEquals("myruntime", moduleDefinition.getRuntimeFramework());
    }

    public void testReadModuleDefinitionProperties() throws Exception {
        Document document = XMLDomUtils.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        
        Element locations = document.createElement("config-locations");
        root.appendChild(locations);
        
        Element location1 = document.createElement("config-location");
        location1.setTextContent("location1");
        locations.appendChild(location1);
        
        Element attributes = document.createElement("attributes");
        root.appendChild(attributes);
        
        Element attribute1 = document.createElement("attribute");
        attribute1.setAttribute("name", "prop1");
        attribute1.setTextContent("value1");
        attributes.appendChild(attribute1);
        
        Element attribute2 = document.createElement("attribute");
        attribute2.setAttribute("name", "prop2");
        attribute2.setTextContent("value2");
        attributes.appendChild(attribute2);
        
        Element location2 = document.createElement("config-location");
        location2.setTextContent("location2");
        locations.appendChild(location2);
        
        Element dependsOn = document.createElement("depends-on");
        dependsOn.setTextContent("module1,module2, module3 , module4 module5");
        root.appendChild(dependsOn);
        
        Element runtime = document.createElement("runtime");
        runtime.setTextContent("myruntime");
        root.appendChild(runtime);
        
        final StringWriter writer = new StringWriter();
        XMLDomUtils.output(writer, document);
        System.out.println(writer);
        
        Properties properties = new Properties();
        reader.readModuleDefinitionProperties(properties, "mymodule", root);
        System.out.println(properties);
        assertEquals("location1,location2", properties.get("config-locations"));
        assertEquals("module1,module2,module3,module4,module5", properties.get("depends-on"));
        
        ModuleDefinition moduleDefinition = reader.readModuleDefinition(new SimpleModuleDefinition("parent"), "mymodule", root);
        assertEquals(Arrays.asList(new String[]{ "location1", "location2"}), moduleDefinition.getConfigLocations());
        assertEquals(Arrays.asList(new String[]{ "parent", "module1", "module2", "module3", "module4", "module5"}), moduleDefinition.getDependentModuleNames());
        
        Map<String,String> expectedAttributes = new HashMap<String,String>();
        expectedAttributes.put("prop1", "value1");
        expectedAttributes.put("prop2", "value2");
        assertEquals(expectedAttributes, moduleDefinition.getAttributes());
        assertEquals("myruntime", moduleDefinition.getRuntimeFramework());
    }
    
}
