package org.impalaframework.plugin.spec.transition;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.same;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.impalaframework.plugin.spec.transition.SharedSpecProviders.newTest1;
import static org.impalaframework.plugin.spec.transition.SharedSpecProviders.plugin1;
import junit.framework.TestCase;

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.modification.PluginTransition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class PluginStateManagerMockTest extends TestCase {

	private ApplicationContextLoader loader;
	private ConfigurableApplicationContext parentContext;
	private ConfigurableApplicationContext childContext;
	private PluginStateManager tm;
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

		tm = new PluginStateManager();
		tm.setApplicationContextLoader(loader);
		
		TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
		loadTransitionProcessor = new LoadTransitionProcessor(loader);
		unloadTransitionProcessor = new UnloadTransitionProcessor();
		transitionProcessors.addTransitionProcessor(PluginTransition.UNLOADED_TO_LOADED, loadTransitionProcessor);
		transitionProcessors.addTransitionProcessor(PluginTransition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
		tm.setTransitionProcessorRegistry(transitionProcessors);
	}
	
	
	public void testLoadParent() {

		ParentSpec parentSpec = newTest1().getPluginSpec();
		//expectations (round 1 - loading of parent)
		expect(loader.loadContext(eq(parentSpec), (ApplicationContext) isNull())).andReturn(parentContext);
		
		replayMocks();
		loadTransitionProcessor.process(tm, null, null, parentSpec);
		
		assertSame(parentContext, tm.getParentContext());
		
		verifyMocks();
		resetMocks();

		//now test loading plugin1
		PluginSpec pluginSpec = parentSpec.getPlugin(plugin1);
		
		//expectations (round 2 - loading of child)
		expect(loader.loadContext(eq(pluginSpec), same(parentContext))).andReturn(childContext);

		replayMocks();
		loadTransitionProcessor.process(tm, null, null, pluginSpec);
		
		assertSame(parentContext, tm.getParentContext());
		assertSame(childContext, tm.getPlugin(plugin1));
		
		verifyMocks();
		resetMocks();
		
		//now load plugins again - nothing happens
		replayMocks();
		loadTransitionProcessor.process(tm, null, null, parentSpec);
		loadTransitionProcessor.process(tm, null, null, pluginSpec);
		
		assertSame(parentContext, tm.getParentContext());
		assertSame(childContext, tm.getPlugin(plugin1));
		
		verifyMocks();
		resetMocks();

		//now test unloading plugin 1

		//expectations (round 3 - unloading of child)
		childContext.close();
		
		replayMocks();
		unloadTransitionProcessor.process(tm, null, null, pluginSpec);
		verifyMocks();
		
		assertNull(tm.getPlugin(plugin1));
		
		resetMocks();

		//expectations (round 4 - unloading of parent)
		parentContext.close();
		
		replayMocks();
		unloadTransitionProcessor.process(tm, null, null, parentSpec);
		verifyMocks();
		
		assertNull(tm.getPlugin(plugin1));
		assertNull(tm.getParentContext());
		
		resetMocks();
		
		//now attempt to unload child again - does nothing
		replayMocks();
		unloadTransitionProcessor.process(tm, null, null, pluginSpec);
		unloadTransitionProcessor.process(tm, null, null, parentSpec);
		verifyMocks();
	}
}
