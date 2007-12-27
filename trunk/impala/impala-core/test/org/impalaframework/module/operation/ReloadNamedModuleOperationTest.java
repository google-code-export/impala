package org.impalaframework.module.operation;

import static org.easymock.EasyMock.expect;

import java.util.Collections;

import org.impalaframework.module.modification.ModuleState;

public class ReloadNamedModuleOperationTest extends BaseModuleOperationTest {

	protected ModuleOperation getOperation() {
		return new ReloadNamedModuleOperation(moduleManagementFactory);
	}
	
	public final void testInvalidArgs() {
		try {
			operation.execute(new ModuleOperationInput(null, null, null));
		}
		catch (IllegalArgumentException e) {
			assertEquals("moduleName is required as it specifies the name of the module to reload in org.impalaframework.module.operation.ReloadNamedModuleOperation", e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public final void testExecute() {
		
		expect(moduleManagementFactory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleManagementFactory.getModificationExtractorRegistry()).andReturn(modificationExtractorRegistry);

		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(originalDefinition);
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
		
		expect(newDefinition.findChildDefinition("mymodule", true)).andReturn(newDefinition);
		newDefinition.setState(ModuleState.STALE);
		
		expect(strictModificationExtractor.getTransitions(originalDefinition, newDefinition)).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);
		expect(transitionSet.getModuleTransitions()).andReturn(Collections.EMPTY_LIST);
		
		replayMocks();

		//returns fallse because no module transitions found
		assertEquals(ModuleOperationResult.FALSE, operation.execute(new ModuleOperationInput(null, null, "mymodule")));
		
		verifyMocks();
	}

}
