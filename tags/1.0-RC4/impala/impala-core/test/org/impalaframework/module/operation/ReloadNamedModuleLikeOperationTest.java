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

package org.impalaframework.module.operation;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.impalaframework.module.spi.TransitionResultSet;
import org.impalaframework.module.spi.TransitionResultSetTest;

public class ReloadNamedModuleLikeOperationTest extends BaseModuleOperationTest {

    private ModuleOperationRegistry moduleOperationRegistry;
    
    @Override
    protected void setUp() throws Exception {
        moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
        super.setUp();
    }

    protected LockingModuleOperation getOperation() {
        ReloadModuleNamedLikeOperation operation = new ReloadModuleNamedLikeOperation();
        operation.setModificationExtractorRegistry(modificationExtractorRegistry);
        operation.setFrameworkLockHolder(frameworkLockHolder);
        operation.setModuleOperationRegistry(moduleOperationRegistry);
        return operation;
    }
    
    public final void testInvalidArgs() {
        try {
            operation.execute(application, new ModuleOperationInput(null, null, null));
        }
        catch (IllegalArgumentException e) {
            assertEquals("moduleName is required as it specifies the name used to match the module to reload in org.impalaframework.module.operation.ReloadModuleNamedLikeOperation", e.getMessage());
        }
    }
    
    public final void testExecuteFound() {
        
        ModuleOperation moduleOperation = createMock(ModuleOperation.class);
        
        expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
        
        expect(newDefinition.findChildDefinition("mymodule", false)).andReturn(newDefinition);

        expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadNamedModuleOperation)).andReturn(moduleOperation);
        
        expect(newDefinition.getName()).andReturn("mymodule2");
        TransitionResultSet transitionResultSet = TransitionResultSetTest.newSuccessTransitionResultSet();
        expect(moduleOperation.execute(application, new ModuleOperationInput(null, null, "mymodule2"))).andReturn(new ModuleOperationResult(transitionResultSet));
        
        replayMocks();
        replay(moduleOperationRegistry);
        replay(moduleOperation);

        ModuleOperationResult result = operation.doExecute(application, new ModuleOperationInput(null, null, "mymodule"));
        assertEquals(true, result.isSuccess());
        assertEquals("mymodule2", result.getOutputParameters().get("moduleName"));
        
        verifyMocks();
        verify(moduleOperationRegistry);
        verify(moduleOperation);
    }
    
    public final void testExecuteNotFound() {
        
        ModuleOperationRegistry moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
        ModuleOperation moduleOperation = createMock(ModuleOperation.class);
        
        expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
        
        expect(newDefinition.findChildDefinition("mymodule", false)).andReturn(null);
        
        replayMocks();
        replay(moduleOperationRegistry);
        replay(moduleOperation);

        ModuleOperationResult result = operation.doExecute(application, new ModuleOperationInput(null, null, "mymodule"));
        assertFalse(result.hasResults());
        
        verifyMocks();
        verify(moduleOperationRegistry);
        verify(moduleOperation);
    }

}
