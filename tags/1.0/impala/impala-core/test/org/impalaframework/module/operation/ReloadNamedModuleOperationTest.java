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

import java.util.Collections;

import org.impalaframework.module.ModuleState;
import org.impalaframework.module.spi.TransitionResultSet;

public class ReloadNamedModuleOperationTest extends BaseModuleOperationTest {

    protected LockingModuleOperation getOperation() {
        ReloadNamedModuleOperation operation = new ReloadNamedModuleOperation();
        operation.setModificationExtractorRegistry(modificationExtractorRegistry);
        operation.setFrameworkLockHolder(frameworkLockHolder);
        operation.setTransitionManager(transitionManager);
        return operation;
    }
    
    public final void testInvalidArgs() {
        try {
            operation.execute(application, new ModuleOperationInput(null, null, null));
        }
        catch (IllegalArgumentException e) {
            assertEquals("moduleName is required as it specifies the name of the module to reload in org.impalaframework.module.operation.ReloadNamedModuleOperation", e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public final void testExecute() {
        expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(originalDefinition);
        expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
        
        expect(newDefinition.findChildDefinition("mymodule", true)).andReturn(newDefinition);
        newDefinition.setState(ModuleState.STALE);
        
        expect(strictModificationExtractor.getTransitions(application, originalDefinition, newDefinition)).andReturn(transitionSet);
        expect(transitionManager.processTransitions(moduleStateHolder, application, transitionSet)).andReturn(new TransitionResultSet());
        expect(transitionSet.getModuleTransitions()).andReturn(Collections.EMPTY_LIST);
        
        replayMocks();

        //returns fallse because no module transitions found
        assertEquals(ModuleOperationResult.EMPTY, operation.doExecute(application, new ModuleOperationInput(null, null, "mymodule")));
        
        verifyMocks();
    }

}
