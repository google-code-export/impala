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

package org.impalaframework.spring.jmx;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.HashMap;

import junit.framework.TestCase;

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.impalaframework.module.spi.TransitionSet;

public class ModuleManagementOperationsTest extends TestCase {

	private ModuleOperationRegistry moduleOperationRegistry;
	
	private ModuleOperation moduleOperation;

	private ModuleManagementOperations operations;

	private RootModuleDefinition rootModuleDefinition;
	
	private TransitionSet moduleModificationSet;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		operations = new ModuleManagementOperations();
		moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
		moduleOperation = createMock(ModuleOperation.class);
		rootModuleDefinition = createMock(RootModuleDefinition.class);
		moduleModificationSet = createMock(TransitionSet.class);
		operations.setModuleOperationRegistry(moduleOperationRegistry);
	}

	public void testReload() {

		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation)).andReturn(moduleOperation);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("moduleName", "somePlugin");
		expect(moduleOperation.execute(new ModuleOperationInput(null, null, "someplugin"))).andReturn(new ModuleOperationResult(true, resultMap));
		replayMocks();

		assertEquals("Successfully reloaded somePlugin", operations.reloadModule("someplugin"));

		verifyMocks();
	}
	
	public void testModuleNotFound() {
		
		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation)).andReturn(moduleOperation);
		expect(moduleOperation.execute(new ModuleOperationInput(null, null, "someplugin"))).andReturn(new ModuleOperationResult(false));

		replayMocks();

		assertEquals("Could not find module someplugin", operations.reloadModule("someplugin"));

		verifyMocks();
	}
	
	public void testThrowException() {

		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation)).andReturn(moduleOperation);
		expect(moduleOperation.execute(new ModuleOperationInput(null, null, "someplugin"))).andThrow(new IllegalStateException());

		replayMocks();

		assertTrue(operations.reloadModule("someplugin").contains("IllegalStateException"));

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
