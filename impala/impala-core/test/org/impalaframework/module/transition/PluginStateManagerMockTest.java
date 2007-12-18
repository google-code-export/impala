package org.impalaframework.module.transition;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.same;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.impalaframework.module.transition.SharedSpecProviders.newTest1;
import static org.impalaframework.module.transition.SharedSpecProviders.plugin1;
import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.modification.ModuleTransition;
import org.impalaframework.module.transition.DefaultPluginStateManager;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class PluginStateManagerMockTest extends TestCase {

	private ApplicationContextLoader loader;
	private ConfigurableApplicationContext parentContext;
	private ConfigurableApplicationContext childContext;
	private DefaultPluginStateManager tm;
	private LoadTransitionProcessor loadTransitionProcessor;
	private UnloadTransitionProcessor unloadTransitionProcessor;
	
	private void replayMocks() {
		replay(loader);
		replay(parentContext);
		replay(childContext);
	}
	
	private void verifyMocks() {
		verify(loader);
		verify(parentContext);
		verify(childContext);
	}
	
	private void resetMocks() {
		reset(loader);
		reset(parentContext);
		reset(childContext);
	}

	public void setUp() {

		loader = createMock(ApplicationContextLoader.class);
		parentContext = createMock(ConfigurableApplicationContext.class);
		childContext = createMock(ConfigurableApplicationContext.class);

		tm = new DefaultPluginStateManager();
		
		TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
		loadTransitionProcessor = new LoadTransitionProcessor(loader);
		unloadTransitionProcessor = new UnloadTransitionProcessor();
		transitionProcessors.addTransitionProcessor(ModuleTransition.UNLOADED_TO_LOADED, loadTransitionProcessor);
		transitionProcessors.addTransitionProcessor(ModuleTransition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
		tm.setTransitionProcessorRegistry(transitionProcessors);
	}
	
	
	public void testLoadParent() {

		RootModuleDefinition rootModuleDefinition = newTest1().getModuleDefintion();
		//expectations (round 1 - loading of parent)
		expect(loader.loadContext(eq(rootModuleDefinition), (ApplicationContext) isNull())).andReturn(parentContext);
		
		replayMocks();
		loadTransitionProcessor.process(tm, null, null, rootModuleDefinition);
		
		assertSame(parentContext, tm.getParentContext());
		
		verifyMocks();
		resetMocks();

		//now test loading plugin1
		ModuleDefinition moduleDefinition = rootModuleDefinition.getPlugin(plugin1);
		
		//expectations (round 2 - loading of child)
		expect(loader.loadContext(eq(moduleDefinition), same(parentContext))).andReturn(childContext);

		replayMocks();
		loadTransitionProcessor.process(tm, null, null, moduleDefinition);
		
		assertSame(parentContext, tm.getParentContext());
		assertSame(childContext, tm.getPlugin(plugin1));
		
		verifyMocks();
		resetMocks();
		
		//now load plugins again - nothing happens
		replayMocks();
		loadTransitionProcessor.process(tm, null, null, rootModuleDefinition);
		loadTransitionProcessor.process(tm, null, null, moduleDefinition);
		
		assertSame(parentContext, tm.getParentContext());
		assertSame(childContext, tm.getPlugin(plugin1));
		
		verifyMocks();
		resetMocks();

		//now test unloading plugin 1

		//expectations (round 3 - unloading of child)
		childContext.close();
		
		replayMocks();
		unloadTransitionProcessor.process(tm, null, null, moduleDefinition);
		verifyMocks();
		
		assertNull(tm.getPlugin(plugin1));
		
		resetMocks();

		//expectations (round 4 - unloading of parent)
		parentContext.close();
		
		replayMocks();
		unloadTransitionProcessor.process(tm, null, null, rootModuleDefinition);
		verifyMocks();
		
		assertNull(tm.getPlugin(plugin1));
		assertNull(tm.getParentContext());
		
		resetMocks();
		
		//now attempt to unload child again - does nothing
		replayMocks();
		unloadTransitionProcessor.process(tm, null, null, moduleDefinition);
		unloadTransitionProcessor.process(tm, null, null, rootModuleDefinition);
		verifyMocks();
	}
}
