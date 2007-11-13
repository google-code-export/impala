package org.impalaframework.plugin.spec.transition;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
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
import org.impalaframework.plugin.spec.ApplicationContextSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class PluginTransitionManagerMockTest extends TestCase {

	private ApplicationContextLoader loader;
	private ConfigurableApplicationContext parentContext;
	private ConfigurableApplicationContext childContext;
	
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
	}
	
	
	public void testLoadParent() {

		PluginTransitionManager tm = new PluginTransitionManager();
		tm.setApplicationContextLoader(loader);
		ParentSpec parentSpec = newTest1().getPluginSpec();
		//expectations (round 1 - loading of parent)
		expect(loader.loadContext(isA(ApplicationContextSet.class), eq(parentSpec), (ApplicationContext) isNull())).andReturn(parentContext);
		
		replayMocks();
		tm.load(parentSpec);
		
		assertSame(parentContext, tm.getParentContext());
		
		verifyMocks();
		resetMocks();

		//now test loading plugin1
		PluginSpec pluginSpec = parentSpec.getPlugin(plugin1);
		
		//expectations (round 2 - loading of child)
		expect(loader.loadContext(isA(ApplicationContextSet.class), eq(pluginSpec), same(parentContext))).andReturn(childContext);

		replayMocks();
		tm.load(pluginSpec);
		
		assertSame(parentContext, tm.getParentContext());
		assertSame(childContext, tm.getPlugin(plugin1));
		
		verifyMocks();
		resetMocks();
		
		//now load plugins again - nothing happens
		replayMocks();
		tm.load(parentSpec);
		tm.load(pluginSpec);
		
		assertSame(parentContext, tm.getParentContext());
		assertSame(childContext, tm.getPlugin(plugin1));
		
		verifyMocks();
		resetMocks();

		//now test unloading plugin 1

		//expectations (round 3 - unloading of child)
		childContext.close();
		
		replayMocks();
		tm.unload(pluginSpec);
		verifyMocks();
		
		assertNull(tm.getPlugin(plugin1));
		
		resetMocks();

		//expectations (round 4 - unloading of parent)
		parentContext.close();
		
		replayMocks();
		tm.unload(parentSpec);
		verifyMocks();
		
		assertNull(tm.getPlugin(plugin1));
		assertNull(tm.getParentContext());
		
		resetMocks();
		
		//now attempt to unload child again - does nothing
		replayMocks();
		tm.unload(pluginSpec);
		tm.unload(parentSpec);
		verifyMocks();
	}
}
