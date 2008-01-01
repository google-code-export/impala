package org.impalaframework.module.operation;

import static org.easymock.EasyMock.expect;

public class CloseRootModuleOperationTest extends BaseModuleOperationTest {

	@Override
	protected ModuleOperation getOperation() {
		CloseRootModuleOperation operation = new CloseRootModuleOperation();
		operation.setModificationExtractorRegistry(modificationExtractorRegistry);
		operation.setModuleStateHolder(moduleStateHolder);
		return operation;
	}
	
	public final void testExecute() {
		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
		
		expect(strictModificationExtractor.getTransitions(originalDefinition, null)).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);
		
		replayMocks();

		assertEquals(ModuleOperationResult.TRUE, operation.execute(new ModuleOperationInput(null, null, null)));
		
		verifyMocks();
		
	}

}
