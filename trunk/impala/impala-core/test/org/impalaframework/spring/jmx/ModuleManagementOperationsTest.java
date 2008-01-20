package org.impalaframework.spring.jmx;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.HashMap;

import junit.framework.TestCase;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.TransitionSet;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;

public class ModuleManagementOperationsTest extends TestCase {

	private ModuleOperationRegistry moduleOperationRegistry;
	
	private ModuleOperation moduleOperation;

	private ModuleManagementOperations operations;

	private RootModuleDefinition rootModuleDefinition;
	
	private TransitionSet pluginModificationSet;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		operations = new ModuleManagementOperations();
		moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
		moduleOperation = createMock(ModuleOperation.class);
		rootModuleDefinition = createMock(RootModuleDefinition.class);
		pluginModificationSet = createMock(TransitionSet.class);
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
	
	public void testPluginNotFound() {
		
		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation)).andReturn(moduleOperation);
		expect(moduleOperation.execute(new ModuleOperationInput(null, null, "someplugin"))).andReturn(new ModuleOperationResult(false));

		replayMocks();

		assertEquals("Could not find plugin someplugin", operations.reloadModule("someplugin"));

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
		replay(pluginModificationSet);
		replay(moduleOperationRegistry);
		replay(moduleOperation);
	}

	private void verifyMocks() {
		verify(pluginModificationSet);
		verify(moduleOperationRegistry);
		verify(moduleOperation);
		verify(rootModuleDefinition);
	}
}
