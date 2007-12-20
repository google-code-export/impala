package org.impalaframework.spring.jmx;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.TransitionSet;

public class ModuleManagementOperationsTest extends TestCase {

	private ModificationExtractor modificationExtractor;

	private ModuleStateHolder moduleStateHolder;

	private ModuleManagementOperations operations;

	private RootModuleDefinition rootModuleDefinition;
	
	private TransitionSet pluginModificationSet;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		operations = new ModuleManagementOperations();
		modificationExtractor = createMock(ModificationExtractor.class);
		moduleStateHolder = createMock(ModuleStateHolder.class);
		rootModuleDefinition = createMock(RootModuleDefinition.class);
		pluginModificationSet = createMock(TransitionSet.class);
		operations.setPluginModificationCalculator(modificationExtractor);
		operations.setPluginStateManager(moduleStateHolder);
	}

	public void testReload() {

		expect(moduleStateHolder.getParentSpec()).andReturn(rootModuleDefinition);
		expect(moduleStateHolder.cloneParentSpec()).andReturn(rootModuleDefinition);
		expect(rootModuleDefinition.findPlugin("someplugin", true)).andReturn(rootModuleDefinition);
		expect(modificationExtractor.reload(rootModuleDefinition, rootModuleDefinition, "someplugin")).andReturn(pluginModificationSet);
		moduleStateHolder.processTransitions(pluginModificationSet);
		
		replayMocks();

		assertEquals("Successfully reloaded someplugin", operations.reloadPlugin("someplugin"));

		verifyMocks();
	}
	
	public void testPluginNotFound() {

		expect(moduleStateHolder.getParentSpec()).andReturn(rootModuleDefinition);
		expect(moduleStateHolder.cloneParentSpec()).andReturn(rootModuleDefinition);
		expect(rootModuleDefinition.findPlugin("someplugin", true)).andReturn(null);
		
		replayMocks();

		assertEquals("Could not find plugin someplugin", operations.reloadPlugin("someplugin"));

		verifyMocks();
	}
	
	public void testThrowException() {

		expect(moduleStateHolder.getParentSpec()).andReturn(rootModuleDefinition);
		expect(moduleStateHolder.cloneParentSpec()).andReturn(rootModuleDefinition);
		expect(rootModuleDefinition.findPlugin("someplugin", true)).andReturn(rootModuleDefinition);
		expect(modificationExtractor.reload(rootModuleDefinition, rootModuleDefinition, "someplugin")).andReturn(pluginModificationSet);
		moduleStateHolder.processTransitions(pluginModificationSet);
		expectLastCall().andThrow(new IllegalStateException());
		
		replayMocks();

		assertTrue(operations.reloadPlugin("someplugin").contains("IllegalStateException"));

		verifyMocks();
	}

	private void replayMocks() {
		replay(rootModuleDefinition);
		replay(pluginModificationSet);
		replay(modificationExtractor);
		replay(moduleStateHolder);
	}

	private void verifyMocks() {
		verify(pluginModificationSet);
		verify(modificationExtractor);
		verify(moduleStateHolder);
		verify(rootModuleDefinition);
	}
}
