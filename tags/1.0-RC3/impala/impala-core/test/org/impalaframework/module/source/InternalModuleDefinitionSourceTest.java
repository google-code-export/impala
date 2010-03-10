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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

public class InternalModuleDefinitionSourceTest extends TestCase {

    private InternalModuleDefinitionSource moduleDefinitionSource;
    private String[] moduleNames = new String[] { 
            "impala-core",
            "sample-module1", 
            "sample-module2", 
            "sample-module3", 
            "sample-module4" };
    private TypeReaderRegistry typeReaderRegistry;
    private StandaloneModuleLocationResolver resolver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.typeReaderRegistry = TypeReaderRegistryFactory.getTypeReaderRegistry();
        this.resolver = new StandaloneModuleLocationResolver();
        this.moduleDefinitionSource = new InternalModuleDefinitionSource(typeReaderRegistry, resolver, moduleNames, true);
    }

    public void testGetModuleDefinition() {
        RootModuleDefinition moduleDefinition = moduleDefinitionSource.getModuleDefinition();
        System.out.println(moduleDefinition);
    }

    public void testGetResourceForModule() throws IOException {
        URL resourceForModule = moduleDefinitionSource.getResourceForModule("sample-module1", "module.properties");
        assertNotNull(resourceForModule);

        InputStream inputStream = resourceForModule.openStream();
        String text = FileCopyUtils.copyToString(new InputStreamReader(inputStream));
        assertTrue(StringUtils.hasText(text));
    }

    public void testGetNoResourceForModule() throws IOException {
        try {
            moduleDefinitionSource.getResourceForModule("sample-module1", "nothere.properties");
            fail();
        }
        catch (ConfigurationException e) {
            assertTrue(e.getMessage().contains(
                    "Application is using internally defined module structure, but no module.properties file is present on the classpath for module 'sample-module1'"));
        }
    }

    public void testCheckParent() {
        try {
            moduleDefinitionSource.checkParent("mymodule", "mymodule");
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("Module 'mymodule' illegally declares itself as parent in module.properties", e.getMessage());
        }
    }
    
    public void testGetRoot() {
        moduleDefinitionSource.buildMaps();
        assertEquals("impala-core", moduleDefinitionSource.determineRootDefinition());
    }
    
    public void testMultipleRoots() {
        moduleDefinitionSource.buildMaps();
        Properties anotherProperties = new Properties();
        moduleDefinitionSource.getModuleProperties().put("another-potential-root", anotherProperties);
        moduleDefinitionSource.getOrphans().add("another-potential-root");
        assertEquals("impala-core", moduleDefinitionSource.determineRootDefinition());
        
        anotherProperties.put(ModuleElementNames.TYPE_ELEMENT, ModuleTypes.ROOT);
        assertEquals("another-potential-root", moduleDefinitionSource.determineRootDefinition());
    }
    
    public void testMultipleRootsWithDifferentOrdering() {
        moduleDefinitionSource.buildMaps();
        Properties anotherProperties = new Properties();
        moduleDefinitionSource.getModuleProperties().put("another-potential-root", anotherProperties);
        moduleDefinitionSource.getOrphans().remove("impala-core");
        moduleDefinitionSource.getOrphans().add("another-potential-root");
        moduleDefinitionSource.getOrphans().add("impala-core");
        assertEquals("another-potential-root", moduleDefinitionSource.determineRootDefinition());
    }
    
    public void testGetSiblings() {

        Properties anotherProperties = new Properties();
        moduleDefinitionSource.getModuleProperties().put("another-potential-root", anotherProperties);
        moduleDefinitionSource.getOrphans().add("another-potential-root");
        moduleDefinitionSource.inspectModules();
        Set<String> siblings = moduleDefinitionSource.getSiblings();
        assertEquals(1, siblings.size());
        //impala-core is a sibling because another-potential-root added before inspectModules was called
        assertTrue(siblings.contains("impala-core"));
    }
    
    public void testNoRoots() {
        moduleDefinitionSource.buildMaps();
        moduleDefinitionSource.getOrphans().remove("impala-core");
        try {
            moduleDefinitionSource.determineRootDefinition();
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("Module hierarchy does not have a root module. At least one module with no parent needs to be specified", e.getMessage());
        }
    }
    
    public void testMap() throws IOException {
        moduleDefinitionSource.loadProperties(moduleNames);
        Map<String, Properties> map = moduleDefinitionSource.getModuleProperties();
        assertEquals(5, map.size());
        for (String key : map.keySet()) {
            assertNotNull(map.get(key));
        }
        
        moduleDefinitionSource.extractParentsAndChildren(moduleNames);
        Map<String, Set<String>> children = moduleDefinitionSource.getChildren();
        Set<String> coreChildren = children.get("impala-core");
        assertEquals(2, coreChildren.size());
        assertTrue(coreChildren.contains("sample-module1"));
        assertTrue(coreChildren.contains("sample-module2"));
        
        Set<String> twoChildren = children.get("sample-module2");
        assertEquals(2, twoChildren.size());
        assertTrue(twoChildren.contains("sample-module3"));
        assertTrue(twoChildren.contains("sample-module4"));
        
        assertNull(children.get("sample-module1"));
        assertNull(children.get("sample-module3"));
        assertNull(children.get("sample-module4"));
        
        Map<String, String> parents = moduleDefinitionSource.getParents();
        assertEquals(5, parents.size());
        assertNull(parents.get("impala-core"));
        assertEquals("impala-core", parents.get("sample-module1"));
        assertEquals("impala-core", parents.get("sample-module2"));
        assertEquals("sample-module2", parents.get("sample-module3"));
        assertEquals("sample-module2", parents.get("sample-module4"));
    }
    
    public void testFindMissingDefinitionsSixOnly() throws Exception {
        InternalModuleDefinitionSource source = new InternalModuleDefinitionSource(typeReaderRegistry, resolver, new String[]{"sample-module6"});
        final RootModuleDefinition rootDefinition = source.getModuleDefinition();
        
        System.out.println(rootDefinition);
        assertNotNull(rootDefinition.findChildDefinition("sample-module4", true));
        assertNotNull(rootDefinition.findChildDefinition("sample-module5", true));
        assertNotNull(rootDefinition.findChildDefinition("sample-module6", true));
        assertNotNull(rootDefinition.findChildDefinition("sample-module2", true));
        assertNotNull(rootDefinition.findChildDefinition("impala-core", true));
    }
    
    public void testFindMissingDefinitionsFourOnly() throws Exception {
        InternalModuleDefinitionSource source = new InternalModuleDefinitionSource(typeReaderRegistry, resolver, new String[]{"sample-module4"});
        System.out.println(source.getModuleDefinition());
        RootModuleDefinition rootDefinition = source.getModuleDefinition();
        
        assertEquals("impala-core", rootDefinition.getName());
        assertNotNull(rootDefinition.findChildDefinition("sample-module4", true));
        assertNotNull(rootDefinition.findChildDefinition("sample-module2", true));
        assertNotNull(rootDefinition.findChildDefinition("impala-core", true));
    }
    
    public void testFindMissingDefinitionsFourAndSix() throws Exception {
        InternalModuleDefinitionSource source = new InternalModuleDefinitionSource(typeReaderRegistry, resolver, new String[]{"sample-module4", "sample-module6"});

        System.out.println(source.getModuleDefinition());
        assertEquals("sample-module5", source.getModuleDefinition().getName());

        assertModulesPresent(source);
    }
    
    public void testFindMissingDefinitionsCoreFourAndSix() throws Exception {
        InternalModuleDefinitionSource source = new InternalModuleDefinitionSource(typeReaderRegistry, resolver, new String[]{"impala-core", "sample-module4", "sample-module6"});

        System.out.println(source.getModuleDefinition());
        assertEquals("impala-core", source.getModuleDefinition().getName());
        

        assertModulesPresent(source);
    }

    private void assertModulesPresent(InternalModuleDefinitionSource source) {
        RootModuleDefinition rootDefinition = source.getModuleDefinition();
        assertNotNull(rootDefinition.findChildDefinition("sample-module4", true));
        assertNotNull(rootDefinition.findChildDefinition("sample-module2", true));
        assertNotNull(rootDefinition.findChildDefinition("impala-core", true));
        assertNotNull(rootDefinition.findChildDefinition("sample-module5", true));
        assertNotNull(rootDefinition.findChildDefinition("sample-module6", true));
    }

}
