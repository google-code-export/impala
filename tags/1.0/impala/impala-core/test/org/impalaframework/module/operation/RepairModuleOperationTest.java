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

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.TransitionSet;

public class RepairModuleOperationTest extends BaseModuleOperationTest {

    protected LockingModuleOperation getOperation() {
        RepairModulesOperation operation = new RepairModulesOperation();
        operation.setModificationExtractorRegistry(modificationExtractorRegistry);
        operation.setFrameworkLockHolder(frameworkLockHolder);
        return operation;
    }

    protected ModificationExtractor getModificationExtractor() {
        return repairModificationExtractor;
    }

    protected RootModuleDefinition getExistingDefinition() {
        return originalDefinition;
    }
    
    public final void testInvalidArgs() {
        try {
            operation.execute(application, new ModuleOperationInput(null, null, null));
        }
        catch (IllegalArgumentException e) {
            assertEquals("moduleDefinitionSource is required as it specifies the new module definition to apply in org.impalaframework.module.operation.RepairModuleOperation", e.getMessage());
        }
    }
    
    public final void testExecute() {

        ModuleDefinitionSource moduleDefinitionSource = EasyMock.createMock(ModuleDefinitionSource.class);
        
        expect(moduleDefinitionSource.getModuleDefinition()).andReturn(originalDefinition);
        
        SimpleRootModuleDefinition definition = new SimpleRootModuleDefinition("root", "config");
        definition.freeze();
        
        TransitionSet set = new TransitionSet(new ArrayList<ModuleStateChange>(), definition);
        expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(definition);
        
        expect(repairModificationExtractor.getTransitions(application, isA(RootModuleDefinition.class), isA(RootModuleDefinition.class))).andReturn(set);
    
        transitionManager.processTransitions(eq(moduleStateHolder), application, isA(TransitionSet.class));
        
        replayMocks();
        replay(moduleDefinitionSource);

        assertEquals(ModuleOperationResult.EMPTY, operation.doExecute(application, new ModuleOperationInput(moduleDefinitionSource, null, null)));
        
        verifyMocks();
        verify(moduleDefinitionSource);
    }

}
