package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.plugin.bootstrap.ModuleManagementSource;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculatorRegistry;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.modification.StrictPluginModificationCalculator;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;

public class WebPluginReloaderTest extends TestCase {

	private ServletContext servletContext;

	private WebPluginReloader reloader;

	private ModuleManagementSource impalaBootstrapFactory;

	private PluginSpecProvider pluginSpecBuilder;

	private PluginStateManager pluginStateManager;

	private PluginModificationCalculatorRegistry calculatorRegistry;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		impalaBootstrapFactory = createMock(ModuleManagementSource.class);
		pluginSpecBuilder = createMock(PluginSpecProvider.class);
		pluginStateManager = createMock(PluginStateManager.class);

		calculatorRegistry = new PluginModificationCalculatorRegistry();
		calculatorRegistry.addModificationCalculationType(ModificationCalculationType.STRICT,
				new StrictPluginModificationCalculator());

		reloader = new WebPluginReloader();
		reloader.setServletContext(servletContext);
	}

	public final void testReloadPlugins() {
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(impalaBootstrapFactory);
		expect(servletContext.getAttribute(WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE)).andReturn(pluginSpecBuilder);
		expect(impalaBootstrapFactory.getPluginStateManager()).andReturn(pluginStateManager);
		expect(pluginStateManager.cloneParentSpec()).andReturn(new SimpleParentSpec("parent"));
		expect(pluginSpecBuilder.getPluginSpec()).andReturn(new SimpleParentSpec("parent"));

		expect(impalaBootstrapFactory.getPluginModificationCalculatorRegistry()).andReturn(calculatorRegistry);
		pluginStateManager.processTransitions(isA(PluginTransitionSet.class));

		replayMocks();
		reloader.reloadPlugins();
		verifyMocks();
	}

	public final void testNoFactory() {
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(null);

		replayMocks();
		try {
			reloader.reloadPlugins();
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("No instance of org.impalaframework.plugin.bootstrap.ModuleManagementSource found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.IMPALA_FACTORY_ATTRIBUTE", e.getMessage());
		}
		verifyMocks();
	}

	public final void testNoPluginSpecBuilder() {
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(impalaBootstrapFactory);
		expect(servletContext.getAttribute(WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE)).andReturn(null);

		replayMocks();
		try {
			reloader.reloadPlugins();
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("No instance of org.impalaframework.plugin.builder.PluginSpecBuilder found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE", e.getMessage());
		}
		verifyMocks();
	}

	private void verifyMocks() {
		verify(servletContext);
		verify(impalaBootstrapFactory);
		verify(pluginSpecBuilder);
		verify(pluginStateManager);
	}

	private void replayMocks() {
		replay(servletContext);
		replay(impalaBootstrapFactory);
		replay(pluginSpecBuilder);
		replay(pluginStateManager);
	}

}
