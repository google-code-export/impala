package org.impalaframework.module.operation;

import static org.easymock.EasyMock.expect;

public class CloseRootModuleOperationTest extends BaseModuleOperationTest {

	@Override
	protected ModuleOperation getOperation() {
		return new CloseRootModuleOperation(moduleManagementFactory);
	}
	
	public final void testExecute() {
		
		expect(moduleManagementFactory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleManagementFactory.getModificationExtractorRegistry()).andReturn(modificationExtractorRegistry);

		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
		
		expect(strictModificationExtractor.getTransitions(originalDefinition, null)).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);
		
		replayMock();

		assertEquals(ModuleOperationResult.TRUE, operation.execute(new ModuleOperationInput(null, null, null)));
		
		verifyMocks();
		
	}

}
