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

package org.impalaframework.module.operation;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMock;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.holder.DefaultModuleStateHolder;

public class RemoveModuleOperationTest extends BaseModuleOperationTest {

	protected LockingModuleOperation getOperation() {
		RemoveModuleOperation operation = new RemoveModuleOperation();
		operation.setModificationExtractorRegistry(modificationExtractorRegistry);
		operation.setModuleStateHolder(moduleStateHolder);
		return operation;
	}

	public final void testRemoveModule() {
		
		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
		ModuleDefinition childDefinition = EasyMock.createMock(ModuleDefinition.class);
		expect(newDefinition.findChildDefinition("myModule", true)).andReturn(childDefinition);
		expect(childDefinition.getParentDefinition()).andReturn(newDefinition);
		expect(newDefinition.remove("myModule")).andReturn(childDefinition);
		childDefinition.setParentDefinition(null);
		
		expect(strictModificationExtractor.getTransitions(originalDefinition, newDefinition)).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);

		replayMocks();
		replay(childDefinition);

		assertEquals(ModuleOperationResult.TRUE, operation.doExecute(new ModuleOperationInput(null, null, "myModule")));

		verifyMocks();
		verify(childDefinition);
	}
	
	public final void testRemoveRoot() {
		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
		expect(newDefinition.findChildDefinition("root", true)).andReturn(newDefinition);
		
		expect(strictModificationExtractor.getTransitions(originalDefinition, null)).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);

		replayMocks();

		assertEquals(ModuleOperationResult.TRUE, operation.doExecute(new ModuleOperationInput(null, null, "root")));

		verifyMocks();
	}
	
	public final void testRootIsNull() {
		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(null);

		replayMocks();

		assertEquals(ModuleOperationResult.FALSE, operation.doExecute(new ModuleOperationInput(null, null, "root")));

		verifyMocks();
	}

	public final void testInvalidArgs() {
		try {
			operation.execute(new ModuleOperationInput(null, null, null));
		}
		catch (IllegalArgumentException e) {
			assertEquals(
					"moduleName is required as it specifies the name of the module to remove in org.impalaframework.module.operation.RemoveModuleOperation",
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public final void testExecuteFound() {
		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);

		expect(strictModificationExtractor.getTransitions(originalDefinition, null)).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);
	}

}

class TestPluginStateManager extends DefaultModuleStateHolder {

	@Override
	protected void setRootModuleDefinition(RootModuleDefinition rootModuleDefinition) {
		super.setRootModuleDefinition(rootModuleDefinition);
	}

}
