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
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMock;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.Transition;
import org.impalaframework.module.spi.TransitionResult;
import org.impalaframework.module.spi.TransitionResultSet;

public class RemoveModuleOperationTest extends BaseModuleOperationTest {

    protected LockingModuleOperation getOperation() {
        RemoveModuleOperation operation = new RemoveModuleOperation();
        operation.setModificationExtractorRegistry(modificationExtractorRegistry);
        operation.setFrameworkLockHolder(frameworkLockHolder);
        operation.setTransitionManager(transitionManager);
        return operation;
    }

    public final void testRemoveModule() {
        
        expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
        expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
        ModuleDefinition childDefinition = EasyMock.createMock(ModuleDefinition.class);
        expect(newDefinition.findChildDefinition("myModule", true)).andReturn(childDefinition);
        expect(childDefinition.getParentDefinition()).andReturn(newDefinition);
        expect(newDefinition.removeChildModuleDefinition("myModule")).andReturn(childDefinition);
        childDefinition.setParentDefinition(null);
        
        expect(strictModificationExtractor.getTransitions(application, originalDefinition, newDefinition)).andReturn(transitionSet);
        expect(transitionManager.processTransitions(moduleStateHolder, application, transitionSet)).andReturn(newTransitionResultSet());

        replayMocks();
        replay(childDefinition);

        ModuleOperationResult execute = operation.doExecute(application, new ModuleOperationInput(null, null, "myModule"));
        assertTrue(execute.hasResults());
        assertTrue(execute.isSuccess());

        verifyMocks();
        verify(childDefinition);
    }
    
    public final void testRemoveRoot() {
        expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
        expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
        expect(newDefinition.findChildDefinition("root", true)).andReturn(newDefinition);
        
        expect(strictModificationExtractor.getTransitions(application, originalDefinition, null)).andReturn(transitionSet);
        expect(transitionManager.processTransitions(moduleStateHolder, application, transitionSet)).andReturn(newTransitionResultSet());

        replayMocks();

        ModuleOperationResult result = operation.doExecute(application, new ModuleOperationInput(null, null, "root"));
        assertTrue(result.hasResults());
        assertTrue(result.isSuccess());

        verifyMocks();
    }

    public final void testRootIsNull() {
        expect(moduleStateHolder.getRootModuleDefinition()).andReturn(null);

        replayMocks();

        ModuleOperationResult execute = operation.doExecute(application, new ModuleOperationInput(null, null, "root"));
        assertFalse(execute.hasResults());
        assertFalse(execute.isSuccess());

        verifyMocks();
    }

    public final void testInvalidArgs() {
        try {
            operation.execute(application, new ModuleOperationInput(null, null, null));
        }
        catch (IllegalArgumentException e) {
            assertEquals(
                    "moduleName is required as it specifies the name of the module to remove in org.impalaframework.module.operation.RemoveModuleOperation",
                    e.getMessage());
        }
    }

    public final void testExecuteFound() {
        expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);

        expect(strictModificationExtractor.getTransitions(application, originalDefinition, null)).andReturn(transitionSet);
        transitionManager.processTransitions(moduleStateHolder, application, transitionSet);
    }
    
    private TransitionResultSet newTransitionResultSet() {
        TransitionResultSet result = new TransitionResultSet();
        ModuleStateChange stateChange = new ModuleStateChange(Transition.LOADED_TO_UNLOADED, new SimpleModuleDefinition("myModule"));
        result.addResult(new TransitionResult(stateChange));
        return result;
    }
    
}
