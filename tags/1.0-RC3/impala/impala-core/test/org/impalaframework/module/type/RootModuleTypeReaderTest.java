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
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.util.XMLDomUtils;
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
        assertTrue(definition.getConfigLocations().isEmpty());
    }

    public void testReadModuleDefinition() {
        Properties properties = new Properties();
        properties.setProperty(ModuleElementNames.CONFIG_LOCATIONS_ELEMENT, "loc1,loc2");
        properties.put(ModuleElementNames.DEPENDENCIES_ELEMENT, "module1,module2, module3 , module4 module5");
    
        ModuleDefinition moduleDefinition = reader.readModuleDefinition(null, "rootModule", properties);
        SimpleRootModuleDefinition definition = (SimpleRootModuleDefinition) moduleDefinition;
        assertEquals(Arrays.asList(new String[]{"loc1", "loc2"}), definition.getConfigLocations());
        assertEquals(Arrays.asList(new String[]{ "module1", "module2", "module3", "module4", "module5"}), moduleDefinition.getDependentModuleNames());
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
        
        Element location2 = document.createElement("config-location");
        location2.setTextContent("location2");
        locations.appendChild(location2);
        
        Element dependsOn = document.createElement("depends-on");
        dependsOn.setTextContent("module1,module2, module3 , module4 module5");
        root.appendChild(dependsOn);        
        
        Properties properties = new Properties();
        reader.readModuleDefinitionProperties(properties, "mymodule", root);
        System.out.println(properties);
        assertEquals("location1,location2", properties.get("config-locations"));
        assertEquals("module1,module2,module3,module4,module5", properties.get("depends-on"));
        
        ModuleDefinition moduleDefinition = reader.readModuleDefinition(null, "mymodule", root);
        assertEquals(Arrays.asList(new String[]{ "location1", "location2"}), moduleDefinition.getConfigLocations());
        assertEquals(Arrays.asList(new String[]{ "module1", "module2", "module3", "module4", "module5"}), moduleDefinition.getDependentModuleNames());
    }
    
    
    public void testReadNoLocations() throws Exception {
        Document document = XMLDomUtils.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        
        Properties properties = new Properties();
        reader.readModuleDefinitionProperties(properties, "mymodule", root);
        assertEquals("", properties.get("config-locations"));
        
        ModuleDefinition moduleDefinition = reader.readModuleDefinition(null, "mymodule", root);
        assertTrue(moduleDefinition.getConfigLocations().isEmpty());
    }

}
