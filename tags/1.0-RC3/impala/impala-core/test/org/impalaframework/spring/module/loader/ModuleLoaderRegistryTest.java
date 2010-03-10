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

package org.impalaframework.spring.module.loader;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.spring.module.DelegatingContextLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Phil Zoio
 */
public class ModuleLoaderRegistryTest extends TestCase {
    
    private String rootModuleName = "project1";

    private ModuleLoaderRegistry moduleLoaderRegistry;
    private DelegatingContextLoaderRegistry delegatingContextLoaderRegistry;
    
    @Override
    protected void setUp() throws Exception {
        moduleLoaderRegistry = new ModuleLoaderRegistry();
        delegatingContextLoaderRegistry = new DelegatingContextLoaderRegistry();
    }
    public void testNoModuleLoader() {
        try {
            moduleLoaderRegistry.getModuleLoader("unknowntype");
        }
        catch (NoServiceException e) {
            assertEquals("No instance of org.impalaframework.module.spi.ModuleLoader available for key 'unknowntype'. Available entries: []", e.getMessage());
        }
    }
    
    public void testGetModuleLoader() {
        ApplicationModuleLoader rootModuleLoader = new ApplicationModuleLoader();
        moduleLoaderRegistry.addItem(ModuleTypes.ROOT, rootModuleLoader);
        ApplicationModuleLoader applicationModuleLoader = new ApplicationModuleLoader();
        moduleLoaderRegistry.addItem(ModuleTypes.APPLICATION, applicationModuleLoader);

        ModuleDefinition p = new SimpleRootModuleDefinition(rootModuleName, new String[] { "parent-context.xml" });
        assertTrue(moduleLoaderRegistry.getModuleLoader(p.getType()) instanceof ApplicationModuleLoader);

        DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
            public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
                    ModuleDefinition definition) {
                return null;
            }
        };
        delegatingContextLoaderRegistry.addItem("sometype", delegatingLoader);
        assertSame(delegatingLoader, delegatingContextLoaderRegistry.getDelegatingLoader("sometype"));
    }
    
    public void testSetModuleLoaders() {
        Map<String, ModuleLoader> moduleLoaders = setModuleLoaders();
        
        assertEquals(2, moduleLoaders.size());

        Map<String,DelegatingContextLoader> delegatingLoaders = new HashMap<String, DelegatingContextLoader>();
        DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
            public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
                    ModuleDefinition definition) {
                return null;
            }
        };
        delegatingLoaders.put("key", delegatingLoader);
        delegatingContextLoaderRegistry.setDelegatingLoaders(delegatingLoaders);

        assertEquals(1, delegatingLoaders.size());      
    }
    
    private Map<String, ModuleLoader> setModuleLoaders() {
        Map<String,ModuleLoader> moduleLoaders = new HashMap<String, ModuleLoader>();
        ApplicationModuleLoader rootModuleLoader = new ApplicationModuleLoader();
        moduleLoaders.put(ModuleTypes.ROOT, rootModuleLoader);
        ApplicationModuleLoader applicationModuleLoader = new ApplicationModuleLoader();
        moduleLoaders.put(ModuleTypes.APPLICATION, applicationModuleLoader);
        moduleLoaderRegistry.setModuleLoaders(moduleLoaders);
        return moduleLoaders;
    }
}
