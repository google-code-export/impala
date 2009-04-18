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

package org.impalaframework.module.runtime;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.spi.ModuleRuntime;
import org.impalaframework.module.spi.ModuleStateHolder;

public class DefaultModuleRuntimeManagerTest extends TestCase {

    private ModuleStateHolder moduleStateHolder;
    
    private ModuleRuntime moduleRuntime;
    
    private RuntimeModule runtimeModule;
    
    private ModuleDefinition moduleDefinition;

    private DefaultModuleRuntimeManager manager;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        moduleStateHolder = createMock(ModuleStateHolder.class);
        moduleRuntime = createMock(ModuleRuntime.class);
        moduleDefinition = createMock(ModuleDefinition.class);
        runtimeModule = createMock(RuntimeModule.class);
        
        manager = new DefaultModuleRuntimeManager();
        manager.setModuleStateHolder(moduleStateHolder);
        Map<String, ModuleRuntime> singletonMap = Collections.singletonMap("spring", moduleRuntime);
        manager.setModuleRuntimes(singletonMap);
    }
    
    public void testInitModuleAlreadyPresent() {
        
        expect(moduleDefinition.getName()).andReturn("mymodule");
        //return existing module
        expect(moduleStateHolder.getModule("mymodule")).andReturn(runtimeModule);
        
        replay(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
        
        assertFalse(manager.initModule(moduleDefinition));
        
        verify(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
    }
    
    public void testInitCreateNewModule() {
        
        expect(moduleDefinition.getName()).andReturn("mymodule");
        expect(moduleStateHolder.getModule("mymodule")).andReturn(null);
        expect(moduleDefinition.getRuntimeFramework()).andReturn("spring");
        
        //create new runtime module
        expect(moduleRuntime.loadRuntimeModule(moduleDefinition)).andReturn(runtimeModule);
        moduleStateHolder.putModule("mymodule", runtimeModule);
        
        replay(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
        
        assertTrue(manager.initModule(moduleDefinition));
        
        verify(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
    }
    
    public void testGetRuntimeFramework() {
        
        expect(moduleDefinition.getRuntimeFramework()).andReturn("duff");
        
        replay(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
        
        try {
            manager.getModuleRuntime(moduleDefinition);
            fail();
        } catch (NoServiceException e) {
            assertEquals("No instance of org.impalaframework.module.spi.ModuleRuntime available for key 'duff'. Available entries: [spring]", e.getMessage());
        }
        
        verify(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
    }
    
    public void testCloseModuleNull() {
        
        expect(moduleDefinition.getName()).andReturn("mymodule");
        //no module apparently present
        expect(moduleStateHolder.removeModule("mymodule")).andReturn(null);
        
        replay(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
        
        assertTrue(manager.closeModule(moduleDefinition));
        
        verify(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
    }
    
    public void testCloseModuleNotNull() {
        
        expect(moduleDefinition.getName()).andReturn("mymodule");
        expect(moduleStateHolder.removeModule("mymodule")).andReturn(runtimeModule);
        expect(moduleDefinition.getRuntimeFramework()).andReturn("spring");
        moduleRuntime.closeModule(runtimeModule);
        
        replay(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
        
        assertTrue(manager.closeModule(moduleDefinition));
        
        verify(moduleRuntime, moduleStateHolder, moduleDefinition, runtimeModule);
    }

}
