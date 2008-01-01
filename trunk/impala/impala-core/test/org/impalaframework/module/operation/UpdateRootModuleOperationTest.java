package org.impalaframework.module.operation;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMock;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModificationExtractor;

public class UpdateRootModuleOperationTest extends BaseModuleOperationTest {

	protected ModuleOperation getOperation() {
		UpdateRootModuleOperation operation = new UpdateRootModuleOperation();
		operation.setModificationExtractorRegistry(modificationExtractorRegistry);
		operation.setModuleStateHolder(moduleStateHolder);
		return operation;
	}

	protected ModificationExtractor getModificationExtractor() {
		return stickyModificationExtractor;
	}

	protected RootModuleDefinition getExistingDefinition() {
		return null;
	}
	
	public final void testInvalidArgs() {
		try {
			operation.execute(new ModuleOperationInput(null, null, null));
		}
		catch (IllegalArgumentException e) {
			assertEquals("moduleDefinitionSource is required as it specifies the new module definition to apply in org.impalaframework.module.operation.UpdateRootModuleOperation", e.getMessage());
		}
	}
	
	public final void testExecute() {

		ModuleDefinitionSource moduleDefinitionSource = EasyMock.createMock(ModuleDefinitionSource.class);
		
		expect(moduleDefinitionSource.getModuleDefinition()).andReturn(newDefinition);
		
		ModificationExtractor modificationExtractor = getModificationExtractor();
		expect(modificationExtractor.getTransitions(null, newDefinition)).andReturn(transitionSet);
		
		RootModuleDefinition existingDefinition = getExistingDefinition();
		expect(strictModificationExtractor.getTransitions(existingDefinition, newDefinition)).andReturn(transitionSet);

		moduleStateHolder.processTransitions(transitionSet);
		
		replayMocks();
		replay(moduleDefinitionSource);

		assertEquals(ModuleOperationResult.TRUE, operation.execute(new ModuleOperationInput(moduleDefinitionSource, null, null)));
		
		verifyMocks();
		verify(moduleDefinitionSource);
	}

}
