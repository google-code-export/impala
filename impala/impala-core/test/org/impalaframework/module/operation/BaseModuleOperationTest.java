package org.impalaframework.module.operation;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;

public class BaseModuleOperationTest extends TestCase {

	protected ModuleManagementFactory moduleManagementFactory;

	protected ModuleOperation operation;

	protected ModuleStateHolder moduleStateHolder;

	protected ModificationExtractor strictModificationExtractor;

	protected ModificationExtractor stickyModificationExtractor;

	protected ModificationExtractorRegistry modificationExtractorRegistry;

	protected RootModuleDefinition originalDefinition;

	protected RootModuleDefinition newDefinition;

	protected TransitionSet transitionSet;

	protected ModuleOperation getOperation() {
		return new AddModuleOperation(moduleManagementFactory);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		moduleManagementFactory = createMock(ModuleManagementFactory.class);
		moduleStateHolder = createMock(ModuleStateHolder.class);
		operation = getOperation();
		strictModificationExtractor = createMock(ModificationExtractor.class);
		stickyModificationExtractor = createMock(ModificationExtractor.class);
		modificationExtractorRegistry = new ModificationExtractorRegistry();
		modificationExtractorRegistry.addModificationExtractorType(ModificationExtractorType.STRICT,
				strictModificationExtractor);
		modificationExtractorRegistry.addModificationExtractorType(ModificationExtractorType.STICKY,
				stickyModificationExtractor);
		originalDefinition = createMock(RootModuleDefinition.class);
		newDefinition = createMock(RootModuleDefinition.class);
		transitionSet = createMock(TransitionSet.class);

	}

	protected void replayMock() {
		replay(moduleManagementFactory);
		replay(moduleStateHolder);
		replay(strictModificationExtractor);
		replay(stickyModificationExtractor);
		replay(originalDefinition);
		replay(newDefinition);
		replay(transitionSet);
	}

	protected void verifyMocks() {
		verify(moduleManagementFactory);
		verify(moduleStateHolder);
		verify(strictModificationExtractor);
		verify(originalDefinition);
		verify(newDefinition);
		verify(transitionSet);
	}

}
