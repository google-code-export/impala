package org.impalaframework.module.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleModificationCalculatorRegistry;
import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.monitor.PluginModificationEvent;
import org.impalaframework.module.monitor.PluginModificationInfo;
import org.impalaframework.module.spec.SimpleRootModuleDefinition;
import org.impalaframework.module.transition.PluginStateManager;
import org.impalaframework.module.web.WebConstants;
import org.impalaframework.module.web.WebPluginModificationListener;

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
		ModuleManagementSource bootstrapFactory = createMock(ModuleManagementSource.class);
		PluginStateManager pluginStateManager = createMock(PluginStateManager.class);

		final WebPluginModificationListener listener = new WebPluginModificationListener(servletContext);
		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("p1");
		
		ModuleModificationCalculator modificationCalculator = createMock(ModuleModificationCalculator.class);
		ModuleModificationCalculatorRegistry registry = new ModuleModificationCalculatorRegistry();
		registry.addModificationCalculationType(ModificationCalculationType.STRICT, modificationCalculator);

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
		ModuleTransitionSet transitionSet = new ModuleTransitionSet(transitions, simpleRootModuleDefinition);
		
		// set expectations
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(bootstrapFactory);
		expect(bootstrapFactory.getPluginStateManager()).andReturn(pluginStateManager);
		expect(pluginStateManager.getParentSpec()).andReturn(simpleRootModuleDefinition);
		expect(bootstrapFactory.getPluginStateManager()).andReturn(pluginStateManager);
		expect(pluginStateManager.cloneParentSpec()).andReturn(simpleRootModuleDefinition);
		expect(bootstrapFactory.getPluginModificationCalculatorRegistry()).andReturn(registry);
		expect(modificationCalculator.reload(simpleRootModuleDefinition, simpleRootModuleDefinition, "p1")).andReturn(transitionSet);
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
