package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginModificationCalculatorRegistry;
import org.impalaframework.plugin.modification.PluginStateChange;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.monitor.PluginModificationEvent;
import org.impalaframework.plugin.monitor.PluginModificationInfo;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;

public class WebPluginModificationListenerTest extends TestCase {

	public final void testEmpty() throws Exception {

		ServletContext servletContext = createMock(ServletContext.class);

		final WebPluginModificationListener listener = new WebPluginModificationListener(servletContext);

		ArrayList<PluginModificationInfo> info = new ArrayList<PluginModificationInfo>();
		PluginModificationEvent event = new PluginModificationEvent(info);

		replay(servletContext);

		listener.pluginModified(event);

		verify(servletContext);

		info.add(new PluginModificationInfo("p1"));
		info.add(new PluginModificationInfo("p2"));
		info.add(new PluginModificationInfo("p2"));

		event = new PluginModificationEvent(info);

	}

	public final void testNonEmpty() throws Exception {


		ArrayList<PluginModificationInfo> info = new ArrayList<PluginModificationInfo>();

		info.add(new PluginModificationInfo("p1"));

		PluginModificationEvent event = new PluginModificationEvent(info);

		ServletContext servletContext = createMock(ServletContext.class);
		ImpalaBootstrapFactory bootstrapFactory = createMock(ImpalaBootstrapFactory.class);
		PluginStateManager pluginStateManager = createMock(PluginStateManager.class);

		final WebPluginModificationListener listener = new WebPluginModificationListener(servletContext);
		SimpleParentSpec simpleParentSpec = new SimpleParentSpec("p1");
		
		PluginModificationCalculator modificationCalculator = createMock(PluginModificationCalculator.class);
		PluginModificationCalculatorRegistry registry = new PluginModificationCalculatorRegistry();
		registry.addModificationCalculationType(ModificationCalculationType.STRICT, modificationCalculator);

		List<PluginStateChange> transitions = new ArrayList<PluginStateChange>();
		PluginTransitionSet transitionSet = new PluginTransitionSet(transitions, simpleParentSpec);
		
		// set expectations
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(bootstrapFactory);
		expect(bootstrapFactory.getPluginStateManager()).andReturn(pluginStateManager);
		expect(pluginStateManager.getParentSpec()).andReturn(simpleParentSpec);
		expect(pluginStateManager.cloneParentSpec()).andReturn(simpleParentSpec);
		expect(bootstrapFactory.getPluginModificationCalculatorRegistry()).andReturn(registry);
		expect(modificationCalculator.reload(simpleParentSpec, simpleParentSpec, "p1")).andReturn(transitionSet);
		pluginStateManager.processTransitions(transitionSet);

		replay(servletContext);
		replay(bootstrapFactory);
		replay(pluginStateManager);
		replay(modificationCalculator);

		listener.pluginModified(event);

		verify(servletContext);
		verify(bootstrapFactory);
		verify(pluginStateManager);
		verify(modificationCalculator);

	}
}
