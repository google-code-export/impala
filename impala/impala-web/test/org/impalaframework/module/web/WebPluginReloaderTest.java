package org.impalaframework.module.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleModificationExtractorRegistry;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.modification.StrictModuleModificationExtractor;
import org.impalaframework.module.transition.ModuleStateManager;
import org.impalaframework.module.web.WebConstants;
import org.impalaframework.module.web.WebPluginReloader;

public class WebPluginReloaderTest extends TestCase {

	private ServletContext servletContext;

	private WebPluginReloader reloader;

	private ModuleManagementSource impalaBootstrapFactory;

	private ModuleDefinitionSource pluginSpecBuilder;

	private ModuleStateManager moduleStateManager;

	private ModuleModificationExtractorRegistry calculatorRegistry;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		impalaBootstrapFactory = createMock(ModuleManagementSource.class);
		pluginSpecBuilder = createMock(ModuleDefinitionSource.class);
		moduleStateManager = createMock(ModuleStateManager.class);

		calculatorRegistry = new ModuleModificationExtractorRegistry();
		calculatorRegistry.addModificationCalculationType(ModificationExtractorType.STRICT,
				new StrictModuleModificationExtractor());

		reloader = new WebPluginReloader();
		reloader.setServletContext(servletContext);
	}

	public final void testReloadPlugins() {
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(impalaBootstrapFactory);
		expect(servletContext.getAttribute(WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE)).andReturn(pluginSpecBuilder);
		expect(impalaBootstrapFactory.getPluginStateManager()).andReturn(moduleStateManager);
		expect(moduleStateManager.cloneParentSpec()).andReturn(new SimpleRootModuleDefinition("parent"));
		expect(pluginSpecBuilder.getModuleDefinition()).andReturn(new SimpleRootModuleDefinition("parent"));

		expect(impalaBootstrapFactory.getPluginModificationCalculatorRegistry()).andReturn(calculatorRegistry);
		moduleStateManager.processTransitions(isA(ModuleTransitionSet.class));

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
			assertEquals("No instance of org.impalaframework.module.bootstrap.ModuleManagementSource found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.IMPALA_FACTORY_ATTRIBUTE", e.getMessage());
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
			assertEquals("No instance of org.impalaframework.module.builder.PluginSpecBuilder found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE", e.getMessage());
		}
		verifyMocks();
	}

	private void verifyMocks() {
		verify(servletContext);
		verify(impalaBootstrapFactory);
		verify(pluginSpecBuilder);
		verify(moduleStateManager);
	}

	private void replayMocks() {
		replay(servletContext);
		replay(impalaBootstrapFactory);
		replay(pluginSpecBuilder);
		replay(moduleStateManager);
	}

}
