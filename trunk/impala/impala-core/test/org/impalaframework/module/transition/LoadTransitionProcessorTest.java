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

package org.impalaframework.module.transition;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collection;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleRuntimeManager;
import org.impalaframework.module.spi.TestApplicationManager;

public class LoadTransitionProcessorTest extends TestCase {

    private LoadTransitionProcessor processor;
    private ModuleRuntimeManager moduleRuntimeManager;
    private Application application;

    public void setUp() {
        processor = new LoadTransitionProcessor();
        moduleRuntimeManager = createMock(ModuleRuntimeManager.class);
        processor.setModuleRuntimeManager(moduleRuntimeManager);
        application = TestApplicationManager.newApplicationManager().getCurrentApplication();
    }
    
    public void testProcess() {
        
        SimpleRootModuleDefinition a = new SimpleRootModuleDefinition("a", (String)null);
        ModuleDefinition a1 = newDefinition(a, "a1", null);
        ModuleDefinition a2 = newDefinition(a1, "a2", null);
        ModuleDefinition a3 = newDefinition(a2, "a3", null);
        ModuleDefinition a4 = newDefinition(a3, "a4", null);
        
        Collection<ModuleDefinition> modules = ModuleDefinitionUtils.getDependentModules(a, "a");
        
        expect(moduleRuntimeManager.initModule(application, a1)).andReturn(true);
        expect(moduleRuntimeManager.initModule(application, a2)).andReturn(false);
        
        replay(moduleRuntimeManager);
        
        for (ModuleDefinition moduleDefinition : modules) {
            processor.process(application, a, moduleDefinition);
        }
        
        verify(moduleRuntimeManager);
        
        assertEquals(ModuleState.LOADED, a1.getState());
        assertEquals(ModuleState.ERROR, a2.getState());
        assertEquals(ModuleState.DEPENDENCY_FAILED, a3.getState());
        assertEquals(ModuleState.DEPENDENCY_FAILED, a4.getState());
        
    }
    
    public void testThrowException() throws Exception {
        
        SimpleRootModuleDefinition a = new SimpleRootModuleDefinition("a", (String)null);
        
        expect(moduleRuntimeManager.initModule(application, a)).andThrow(new RuntimeException());
        
        replay(moduleRuntimeManager);
        
        try {
            processor.process(application, a, a);
            fail();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        
        verify(moduleRuntimeManager);
        
        assertEquals(ModuleState.ERROR, a.getState());
    }
    
    private ModuleDefinition newDefinition(ModuleDefinition parent, final String name, String dependencies) {
        ModuleDefinition definition = new SimpleModuleDefinition(parent, name, ModuleTypes.APPLICATION, null, dependencies == null ? new String[0] : dependencies.split("'"), null, null);
        definition.setState(ModuleState.LOADING);
        return definition;
    }

}
