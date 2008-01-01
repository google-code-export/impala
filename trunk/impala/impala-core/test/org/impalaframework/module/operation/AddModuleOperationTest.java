package org.impalaframework.module.operation;

import static org.easymock.EasyMock.expect;

import org.impalaframework.module.definition.SimpleModuleDefinition;

public class AddModuleOperationTest extends BaseModuleOperationTest {

	protected ModuleOperation getOperation() {
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

		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
		
		expect(stickyModificationExtractor.getTransitions(originalDefinition, newDefinition)).andReturn(transitionSet);
		newDefinition.add(moduleDefinition);
		moduleStateHolder.processTransitions(transitionSet);
		
		replayMocks();

		assertEquals(ModuleOperationResult.TRUE, operation.execute(new ModuleOperationInput(null, moduleDefinition, null)));
		
		verifyMocks();
	}

}
