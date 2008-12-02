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

import org.impalaframework.module.definition.SimpleModuleDefinition;

public class AddModuleOperationTest extends BaseModuleOperationTest {

	protected LockingModuleOperation getOperation() {
		AddModuleOperation operation = new AddModuleOperation();
		operation.setModificationExtractorRegistry(modificationExtractorRegistry);
		operation.setModuleStateHolder(moduleStateHolder);
		return operation;
	}
	
	public final void testInvalidArgs() {
		try {
			operation.execute(new ModuleOperationInput(null, null, null));
		}
		catch (IllegalArgumentException e) {
			assertEquals("moduleName is required as it specifies the name of the module to add in org.impalaframework.module.operation.AddModuleOperation", e.getMessage());
		}
	}
	
	public final void testExecute() {

		SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("mymodule");

		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(originalDefinition);
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
		
		expect(stickyModificationExtractor.getTransitions(originalDefinition, newDefinition)).andReturn(transitionSet);
		
		newDefinition.add(moduleDefinition);
		
		moduleStateHolder.processTransitions(transitionSet);
		
		replayMocks();

		assertEquals(ModuleOperationResult.TRUE, operation.doExecute(new ModuleOperationInput(null, moduleDefinition, null)));
		
		verifyMocks();
	}

}
