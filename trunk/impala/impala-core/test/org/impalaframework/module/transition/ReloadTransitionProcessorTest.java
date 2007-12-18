package org.impalaframework.module.transition;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateHolder;
import org.impalaframework.module.transition.ReloadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessor;

public class ReloadTransitionProcessorTest extends TestCase {

	private ReloadTransitionProcessor processor;

	private TransitionProcessor loadTransitionProcessor;

	private TransitionProcessor unloadTransitionProcessor;

	private ModuleStateHolder moduleStateHolder;

	private RootModuleDefinition p1;

	private RootModuleDefinition p2;

	private ModuleDefinition plugin;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		processor = new ReloadTransitionProcessor();
		loadTransitionProcessor = createMock(TransitionProcessor.class);
		unloadTransitionProcessor = createMock(TransitionProcessor.class);
		processor.setLoadTransitionProcessor(loadTransitionProcessor);
		processor.setUnloadTransitionProcessor(unloadTransitionProcessor);
		moduleStateHolder = createMock(ModuleStateHolder.class);

		p1 = new SimpleRootModuleDefinition("p1");
		p2 = new SimpleRootModuleDefinition("p1");
		plugin = new SimpleModuleDefinition(p2, "p3");
	}

	public final void testBothTrue() {

		expect(unloadTransitionProcessor.process(moduleStateHolder, p1, p2, plugin)).andReturn(true);
		expect(loadTransitionProcessor.process(moduleStateHolder, p1, p2, plugin)).andReturn(true);

		replayMocks();

		assertTrue(processor.process(moduleStateHolder, p1, p2, plugin));

		verifyMocks();
	}

	public final void testUnloadFalse() {

		expect(unloadTransitionProcessor.process(moduleStateHolder, p1, p2, plugin)).andReturn(false);

		replayMocks();

		assertFalse(processor.process(moduleStateHolder, p1, p2, plugin));

		verifyMocks();
	}

	public final void testLoadFalse() {

		expect(unloadTransitionProcessor.process(moduleStateHolder, p1, p2, plugin)).andReturn(true);
		expect(loadTransitionProcessor.process(moduleStateHolder, p1, p2, plugin)).andReturn(false);

		replayMocks();

		assertFalse(processor.process(moduleStateHolder, p1, p2, plugin));

		verifyMocks();
	}

	private void verifyMocks() {
		verify(loadTransitionProcessor);
		verify(unloadTransitionProcessor);
	}

	private void replayMocks() {
		replay(loadTransitionProcessor);
		replay(unloadTransitionProcessor);
	}

}
