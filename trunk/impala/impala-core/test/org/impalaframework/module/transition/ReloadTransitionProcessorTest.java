/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.module.transition;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.TransitionProcessor;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.transition.ReloadTransitionProcessor;

public class ReloadTransitionProcessorTest extends TestCase {

	private ReloadTransitionProcessor processor;

	private TransitionProcessor loadTransitionProcessor;

	private TransitionProcessor unloadTransitionProcessor;

	private ModuleStateHolder moduleStateHolder;

	private RootModuleDefinition rootDefinition;

	private ModuleDefinition definition;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		processor = new ReloadTransitionProcessor();
		loadTransitionProcessor = createMock(TransitionProcessor.class);
		unloadTransitionProcessor = createMock(TransitionProcessor.class);
		processor.setLoadTransitionProcessor(loadTransitionProcessor);
		processor.setUnloadTransitionProcessor(unloadTransitionProcessor);
		moduleStateHolder = createMock(ModuleStateHolder.class);

		rootDefinition = new SimpleRootModuleDefinition("project1", "p1");
		definition = new SimpleModuleDefinition(rootDefinition, "p3");
	}

	public final void testBothTrue() {

		expect(unloadTransitionProcessor.process(moduleStateHolder, rootDefinition, definition)).andReturn(true);
		expect(loadTransitionProcessor.process(moduleStateHolder, rootDefinition, definition)).andReturn(true);

		replayMocks();

		assertTrue(processor.process(moduleStateHolder, rootDefinition, definition));

		verifyMocks();
	}

	public final void testUnloadFalse() {

		expect(unloadTransitionProcessor.process(moduleStateHolder, rootDefinition, definition)).andReturn(false);

		replayMocks();

		assertFalse(processor.process(moduleStateHolder, rootDefinition, definition));

		verifyMocks();
	}

	public final void testLoadFalse() {

		expect(unloadTransitionProcessor.process(moduleStateHolder, rootDefinition, definition)).andReturn(true);
		expect(loadTransitionProcessor.process(moduleStateHolder, rootDefinition, definition)).andReturn(false);

		replayMocks();

		assertFalse(processor.process(moduleStateHolder, rootDefinition, definition));

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
