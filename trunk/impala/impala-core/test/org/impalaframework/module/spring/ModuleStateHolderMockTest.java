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

package org.impalaframework.module.spring;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.same;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.impalaframework.module.holder.SharedModuleDefinitionSources.newTest1;
import static org.impalaframework.module.holder.SharedModuleDefinitionSources.plugin1;

import java.util.Collections;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.module.ApplicationContextLoader;
import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.ModuleStateChangeNotifier;
import org.impalaframework.module.Transition;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.loader.SpringModuleRuntime;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class ModuleStateHolderMockTest extends TestCase {

	private ApplicationContextLoader loader;
	private ConfigurableApplicationContext parentContext;
	private ConfigurableApplicationContext childContext;
	private TestModuleStateHolder moduleStateHolder;
	private LoadTransitionProcessor loadTransitionProcessor;
	private UnloadTransitionProcessor unloadTransitionProcessor;
	private ModuleStateChangeNotifier moduleStateChangeNotifier;
	
	private void replayMocks() {
		replay(loader);
		replay(parentContext);
		replay(childContext);
		replay(moduleStateChangeNotifier);
	}
	
	private void verifyMocks() {
		verify(loader);
		verify(parentContext);
		verify(childContext);
		verify(moduleStateChangeNotifier);
	}
	
	private void resetMocks() {
		reset(loader);
		reset(parentContext);
		reset(childContext);
		reset(moduleStateChangeNotifier);
	}

	public void setUp() {

		loader = createMock(ApplicationContextLoader.class);
		parentContext = createMock(ConfigurableApplicationContext.class);
		childContext = createMock(ConfigurableApplicationContext.class);
		moduleStateChangeNotifier = createMock(ModuleStateChangeNotifier.class);

		moduleStateHolder = new TestModuleStateHolder();
		
		TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
		loadTransitionProcessor = new LoadTransitionProcessor();
		unloadTransitionProcessor = new UnloadTransitionProcessor();
		SpringModuleRuntime moduleRuntime = new SpringModuleRuntime();
		moduleRuntime.setApplicationContextLoader(loader);
		moduleRuntime.setModuleStateHolder(moduleStateHolder);
		loadTransitionProcessor.setModuleRuntime(moduleRuntime);
		
		transitionProcessors.addTransitionProcessor(Transition.UNLOADED_TO_LOADED, loadTransitionProcessor);
		transitionProcessors.addTransitionProcessor(Transition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
		moduleStateHolder.setTransitionProcessorRegistry(transitionProcessors);
	}
	
	public void testNotifier() {
		
		RootModuleDefinition rootModuleDefinition = newTest1().getModuleDefinition();
		ModuleStateChange moduleStateChange = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, rootModuleDefinition);
		moduleStateHolder.setModuleStateChangeNotifier(moduleStateChangeNotifier);
		
		//expectations (round 1 - loading of parent)
		expect(loader.loadContext(eq(rootModuleDefinition), (ApplicationContext) isNull())).andReturn(parentContext);
		moduleStateChangeNotifier.notify(moduleStateHolder, moduleStateChange);
		
		replayMocks();
		
		Set<ModuleStateChange> singleton = Collections.singleton(moduleStateChange);
		TransitionSet transitionSet = new TransitionSet(singleton, rootModuleDefinition);
		moduleStateHolder.processTransitions(transitionSet);
		verifyMocks();
	}
	
	public void testGetRootModuleContext() {
		assertNull(moduleStateHolder.getRootModule());
	}	
	
	public void testLoadParent() {

		RootModuleDefinition rootModuleDefinition = newTest1().getModuleDefinition();
		//expectations (round 1 - loading of parent)
		expect(loader.loadContext(eq(rootModuleDefinition), (ApplicationContext) isNull())).andReturn(parentContext);
		
		replayMocks();
		loadTransitionProcessor.process(moduleStateHolder, null, rootModuleDefinition);
		moduleStateHolder.setRootModuleDefinition(rootModuleDefinition);
		
		assertSame(parentContext, SpringModuleUtils.getRootSpringContext(moduleStateHolder));
		
		verifyMocks();
		resetMocks();

		//now test loading plugin1
		ModuleDefinition moduleDefinition = rootModuleDefinition.getModule(plugin1);
		
		//expectations (round 2 - loading of child)
		expect(loader.loadContext(eq(moduleDefinition), same(parentContext))).andReturn(childContext);

		replayMocks();
		loadTransitionProcessor.process(moduleStateHolder, null, moduleDefinition);
		
		assertSame(parentContext, SpringModuleUtils.getRootSpringContext(moduleStateHolder));
		assertSame(childContext, SpringModuleUtils.getModuleSpringContext(moduleStateHolder, plugin1));
		
		verifyMocks();
		resetMocks();
		
		//now load plugins again - nothing happens
		replayMocks();
		loadTransitionProcessor.process(moduleStateHolder, null, rootModuleDefinition);
		loadTransitionProcessor.process(moduleStateHolder, null, moduleDefinition);
		
		assertSame(parentContext, SpringModuleUtils.getRootSpringContext(moduleStateHolder));
		assertSame(childContext, SpringModuleUtils.getModuleSpringContext(moduleStateHolder, plugin1));
		
		verifyMocks();
		resetMocks();

		//now test unloading plugin 1

		//expectations (round 3 - unloading of child)
		childContext.close();
		
		replayMocks();
		unloadTransitionProcessor.process(moduleStateHolder, null, moduleDefinition);
		verifyMocks();
		
		assertNull(moduleStateHolder.getModule(plugin1));
		
		resetMocks();

		//expectations (round 4 - unloading of parent)
		parentContext.close();
		
		replayMocks();
		unloadTransitionProcessor.process(moduleStateHolder, null, rootModuleDefinition);
		verifyMocks();
		
		assertNull(moduleStateHolder.getModule(plugin1));
		assertNull(moduleStateHolder.getRootModule());
		
		resetMocks();
		
		//now attempt to unload child again - does nothing
		replayMocks();
		unloadTransitionProcessor.process(moduleStateHolder, null, moduleDefinition);
		unloadTransitionProcessor.process(moduleStateHolder, null, rootModuleDefinition);
		verifyMocks();
	}
}

class TestModuleStateHolder extends DefaultModuleStateHolder {

	@Override
	protected void setRootModuleDefinition(
			RootModuleDefinition rootModuleDefinition) {
		super.setRootModuleDefinition(rootModuleDefinition);
	}
	
}
