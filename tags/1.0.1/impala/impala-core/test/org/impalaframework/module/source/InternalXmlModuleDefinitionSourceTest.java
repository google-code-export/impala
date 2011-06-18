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

package org.impalaframework.module.source;

import java.util.Arrays;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.source.InternalXmlModuleDefinitionSource;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.core.io.ClassPathResource;

public class InternalXmlModuleDefinitionSourceTest extends TestCase {
    
    private InternalXmlModuleDefinitionSource moduleDefinitionSource;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        moduleDefinitionSource = new InternalXmlModuleDefinitionSource(new StandaloneModuleLocationResolver());
    }
    
    public void testGetModuleDefinitionGraph() {
        moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/modulegraph.xml"));
        RootModuleDefinition moduleDefinition = moduleDefinitionSource.getModuleDefinition();
        System.out.println(moduleDefinition);
        
        ModuleDefinition definition1 = getDefinition(moduleDefinition, "sample-module1");
        assertEquals(ModuleTypes.APPLICATION, definition1.getType());
        assertEquals(Arrays.asList(new String[0]), definition1.getConfigLocations());
        
        ModuleDefinition definition2 = getDefinition(moduleDefinition, "sample-module2");
        getDefinition(definition2, "sample-module3");
        getDefinition(definition2, "sample-module4");
        
        ModuleDefinition definition5 = moduleDefinition.getSiblingModule("sample-module5");
        assertNotNull(definition5);
        
        ModuleDefinition definition = getDefinition(definition5, "sample-module6");
        assertEquals(Arrays.asList("sample-module3,sample-module4,sample-module5".split(",")), definition.getDependentModuleNames());
    }
    
    public void testGetModuleDefinition() {
        moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/moduledefinition.xml"));
        RootModuleDefinition moduleDefinition = moduleDefinitionSource.getModuleDefinition();
        System.out.println(moduleDefinition);
        
        assertEquals(Arrays.asList(new String[]{"parentTestContext.xml"}), moduleDefinition.getConfigLocations());
        assertEquals(2, moduleDefinition.getChildModuleDefinitions().size());
        
        ModuleDefinition definition1 = getDefinition(moduleDefinition, "sample-module1");
        assertEquals(ModuleTypes.APPLICATION, definition1.getType());
        assertEquals(Arrays.asList(new String[0]), definition1.getConfigLocations());
        
        ModuleDefinition definition2 = getDefinition(moduleDefinition, "sample-module2");
        assertEquals(ModuleTypes.APPLICATION, definition2.getType());
        assertEquals(Arrays.asList(new String[0]), definition2.getConfigLocations());
        
        ModuleDefinition definition3 = getDefinition(definition2, "sample-module3");
        assertEquals(ModuleTypes.APPLICATION, definition3.getType());
        assertEquals(Arrays.asList(new String[0]), definition3.getConfigLocations());
        
        ModuleDefinition definition4 = getDefinition(definition2, "sample-module4");
        assertEquals(Arrays.asList(new String[0]), definition4.getConfigLocations());
    }

    public void testNoNames() throws Exception {
        try {
            moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/nonames.xml"));
            moduleDefinitionSource.getModuleDefinition();
            fail();
        } catch (ConfigurationException e) {
            assertEquals("Resource 'class path resource [xmlinternal/nonames.xml]' contains a non-empty 'names' element, which is illegal when using InternalModuleDefinitionSource", e.getMessage());
        }
    }

    public void testInvalidModules() throws Exception {
        try {
            moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/invalidmodule.xml"));
            moduleDefinitionSource.getModuleDefinition();
            fail();
        } catch (ConfigurationException e) {
            assertEquals("Resource 'class path resource [xmlinternal/invalidmodule.xml]' contains no new properties for module 'sample-module4'. Has this module been declared in the 'names' element?", e.getMessage());
        }
    }
    
    private ModuleDefinition getDefinition(ModuleDefinition moduleDefinition,
            String moduleName) {
        ModuleDefinition def = moduleDefinition.getChildModuleDefinition(moduleName);
        assertNotNull(def);
        return def;
    }

}
