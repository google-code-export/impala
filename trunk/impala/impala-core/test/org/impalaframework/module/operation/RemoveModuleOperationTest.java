package org.impalaframework.module.operation;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMock;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.DefaultModuleStateHolder;

public class RemoveModuleOperationTest extends BaseModuleOperationTest {

	protected ModuleOperation getOperation() {
		return new RemoveModuleOperation(moduleManagementFactory);
	}

	public final void testRemovePlugin() {
		expect(moduleManagementFactory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleManagementFactory.getModificationExtractorRegistry()).andReturn(modificationExtractorRegistry);

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

		assertEquals(ModuleOperationResult.TRUE, operation.execute(new ModuleOperationInput(null, null, "myModule")));

		verifyMocks();
		verify(childDefinition);
	}
	
	public final void testRemoveRoot() {
		expect(moduleManagementFactory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleManagementFactory.getModificationExtractorRegistry()).andReturn(modificationExtractorRegistry);

		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
		expect(newDefinition.findChildDefinition("root", true)).andReturn(newDefinition);
		
		expect(strictModificationExtractor.getTransitions(originalDefinition, null)).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);

		replayMocks();

		assertEquals(ModuleOperationResult.TRUE, operation.execute(new ModuleOperationInput(null, null, "root")));

		verifyMocks();
	}
	
	public final void testRootIsNull() {
		expect(moduleManagementFactory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleManagementFactory.getModificationExtractorRegistry()).andReturn(modificationExtractorRegistry);

		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(null);

		replayMocks();

		assertEquals(ModuleOperationResult.FALSE, operation.execute(new ModuleOperationInput(null, null, "root")));

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

		expect(moduleManagementFactory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleManagementFactory.getModificationExtractorRegistry()).andReturn(modificationExtractorRegistry);

		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);

		expect(strictModificationExtractor.getTransitions(originalDefinition, null)).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);
	}

}

class TestPluginStateManager extends DefaultModuleStateHolder {

	@Override
	protected void setParentSpec(RootModuleDefinition rootModuleDefinition) {
		super.setParentSpec(rootModuleDefinition);
	}

}
