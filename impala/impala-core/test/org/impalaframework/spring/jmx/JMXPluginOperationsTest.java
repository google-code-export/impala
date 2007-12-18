package org.impalaframework.spring.jmx;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateManager;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleTransitionSet;

public class JMXPluginOperationsTest extends TestCase {

	private ModuleModificationExtractor moduleModificationExtractor;

	private ModuleStateManager moduleStateManager;

	private JMXPluginOperations operations;

	private RootModuleDefinition rootModuleDefinition;
	
	private ModuleTransitionSet pluginModificationSet;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		operations = new JMXPluginOperations();
		moduleModificationExtractor = createMock(ModuleModificationExtractor.class);
		moduleStateManager = createMock(ModuleStateManager.class);
		rootModuleDefinition = createMock(RootModuleDefinition.class);
		pluginModificationSet = createMock(ModuleTransitionSet.class);
		operations.setPluginModificationCalculator(moduleModificationExtractor);
		operations.setPluginStateManager(moduleStateManager);
	}

	public void testReload() {

		expect(moduleStateManager.getParentSpec()).andReturn(rootModuleDefinition);
		expect(moduleStateManager.cloneParentSpec()).andReturn(rootModuleDefinition);
		expect(rootModuleDefinition.findPlugin("someplugin", true)).andReturn(rootModuleDefinition);
		expect(moduleModificationExtractor.reload(rootModuleDefinition, rootModuleDefinition, "someplugin")).andReturn(pluginModificationSet);
		moduleStateManager.processTransitions(pluginModificationSet);
		
		replayMocks();

		assertEquals("Successfully reloaded someplugin", operations.reloadPlugin("someplugin"));

		verifyMocks();
	}
	
	public void testPluginNotFound() {

		expect(moduleStateManager.getParentSpec()).andReturn(rootModuleDefinition);
		expect(moduleStateManager.cloneParentSpec()).andReturn(rootModuleDefinition);
		expect(rootModuleDefinition.findPlugin("someplugin", true)).andReturn(null);
		
		replayMocks();

		assertEquals("Could not find plugin someplugin", operations.reloadPlugin("someplugin"));

		verifyMocks();
	}
	
	public void testThrowException() {

		expect(moduleStateManager.getParentSpec()).andReturn(rootModuleDefinition);
		expect(moduleStateManager.cloneParentSpec()).andReturn(rootModuleDefinition);
		expect(rootModuleDefinition.findPlugin("someplugin", true)).andReturn(rootModuleDefinition);
		expect(moduleModificationExtractor.reload(rootModuleDefinition, rootModuleDefinition, "someplugin")).andReturn(pluginModificationSet);
		moduleStateManager.processTransitions(pluginModificationSet);
		expectLastCall().andThrow(new IllegalStateException());
		
		replayMocks();

		assertTrue(operations.reloadPlugin("someplugin").contains("IllegalStateException"));

		verifyMocks();
	}

	private void replayMocks() {
		replay(rootModuleDefinition);
		replay(pluginModificationSet);
		replay(moduleModificationExtractor);
		replay(moduleStateManager);
	}

	private void verifyMocks() {
		verify(pluginModificationSet);
		verify(moduleModificationExtractor);
		verify(moduleStateManager);
		verify(rootModuleDefinition);
	}
}
