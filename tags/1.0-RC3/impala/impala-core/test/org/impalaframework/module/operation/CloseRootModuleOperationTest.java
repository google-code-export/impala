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

import org.impalaframework.module.spi.TransitionResultSet;

public class CloseRootModuleOperationTest extends BaseModuleOperationTest {

    @Override
    protected LockingModuleOperation getOperation() {
        CloseRootModuleOperation operation = new CloseRootModuleOperation();
        operation.setModificationExtractorRegistry(modificationExtractorRegistry);
        operation.setFrameworkLockHolder(frameworkLockHolder);
        operation.setTransitionManager(transitionManager);
        return operation;
    }
    
    public final void testExecute() {
        expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
        
        expect(strictModificationExtractor.getTransitions(application, originalDefinition, null)).andReturn(transitionSet);
        expect(transitionManager.processTransitions(moduleStateHolder, application, transitionSet)).andReturn(new TransitionResultSet());
        
        replayMocks();

        assertEquals(ModuleOperationResult.EMPTY, operation.doExecute(application, new ModuleOperationInput(null, null, null)));
        
        verifyMocks();
        
    }

}
