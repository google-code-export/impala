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

package org.impalaframework.jmx.spring;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.impalaframework.module.spi.TransitionResultSetTest.newFailedTransitionResultSet;
import static org.impalaframework.module.spi.TransitionResultSetTest.newSuccessTransitionResultSet;

import java.util.HashMap;

import junit.framework.TestCase;

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.module.spi.TransitionResultSet;
import org.impalaframework.module.spi.TransitionSet;

public class ModuleManagementOperationsTest extends TestCase {

    private ModuleOperationRegistry moduleOperationRegistry;
    
    private ModuleOperation moduleOperation;

    private ModuleManagementOperations operations;

    private RootModuleDefinition rootModuleDefinition;
    
    private TransitionSet moduleModificationSet;

    private Application application;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        operations = new ModuleManagementOperations();
        moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
        moduleOperation = createMock(ModuleOperation.class);
        rootModuleDefinition = createMock(RootModuleDefinition.class);
        moduleModificationSet = createMock(TransitionSet.class);
        operations.setModuleOperationRegistry(moduleOperationRegistry);
        
        ApplicationManager applicationManager = TestApplicationManager.newApplicationManager(null, null, null);
        application = applicationManager.getCurrentApplication();
        operations.setApplicationManager(applicationManager);
    }

    public void testReload() {

        expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation)).andReturn(moduleOperation);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("moduleName", "moduleName");
        expect(moduleOperation.execute(application, new ModuleOperationInput(null, null, "someModule"))).andReturn(new ModuleOperationResult(newSuccessTransitionResultSet(), resultMap));
        replayMocks();

        assertEquals("Successfully reloaded moduleName", operations.reloadModule("someModule"));

        verifyMocks();
    }

    public void testReloadFailed() {

        expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation)).andReturn(moduleOperation);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("moduleName", "moduleName");
        expect(moduleOperation.execute(application, new ModuleOperationInput(null, null, "someModule"))).andReturn(new ModuleOperationResult(newFailedTransitionResultSet(), resultMap));
        replayMocks();

        assertEquals("One or more module operations failed: stuff went wrong1", operations.reloadModule("someModule"));

        verifyMocks();
    }
    
    public void testModuleNotFound() {
        
        expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation)).andReturn(moduleOperation);
        expect(moduleOperation.execute(application, new ModuleOperationInput(null, null, "someModule"))).andReturn(new ModuleOperationResult(new TransitionResultSet()));

        replayMocks();

        assertEquals("Could not find module someModule", operations.reloadModule("someModule"));

        verifyMocks();
    }
    
    public void testThrowException() {

        expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation)).andReturn(moduleOperation);
        expect(moduleOperation.execute(application, new ModuleOperationInput(null, null, "someModule"))).andThrow(new IllegalStateException());

        replayMocks();

        assertTrue(operations.reloadModule("someModule").contains("IllegalStateException"));

        verifyMocks();
    }

    private void replayMocks() {
        replay(rootModuleDefinition);
        replay(moduleModificationSet);
        replay(moduleOperationRegistry);
        replay(moduleOperation);
    }

    private void verifyMocks() {
        verify(moduleModificationSet);
        verify(moduleOperationRegistry);
        verify(moduleOperation);
        verify(rootModuleDefinition);
    }
}
