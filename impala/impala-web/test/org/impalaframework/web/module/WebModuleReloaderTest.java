package org.impalaframework.web.module;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.TransitionSet;
import org.impalaframework.module.modification.StrictModificationExtractor;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.module.WebModuleReloader;

public class WebModuleReloaderTest extends TestCase {

	private ServletContext servletContext;

	private WebModuleReloader reloader;

	private ModuleManagementFactory impalaBootstrapFactory;

	private ModuleDefinitionSource pluginSpecBuilder;

	private ModuleStateHolder moduleStateHolder;

	private ModificationExtractorRegistry calculatorRegistry;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		impalaBootstrapFactory = createMock(ModuleManagementFactory.class);
		pluginSpecBuilder = createMock(ModuleDefinitionSource.class);
		moduleStateHolder = createMock(ModuleStateHolder.class);

		calculatorRegistry = new ModificationExtractorRegistry();
		calculatorRegistry.addModificationCalculationType(ModificationExtractorType.STRICT,
				new StrictModificationExtractor());

		reloader = new WebModuleReloader();
		reloader.setServletContext(servletContext);
	}

	public final void testReloadPlugins() {
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(impalaBootstrapFactory);
		expect(servletContext.getAttribute(WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE)).andReturn(pluginSpecBuilder);
		expect(impalaBootstrapFactory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(new SimpleRootModuleDefinition("parent"));
		expect(pluginSpecBuilder.getModuleDefinition()).andReturn(new SimpleRootModuleDefinition("parent"));

		expect(impalaBootstrapFactory.getModificationExtractorRegistry()).andReturn(calculatorRegistry);
		moduleStateHolder.processTransitions(isA(TransitionSet.class));

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
			assertEquals("No instance of org.impalaframework.module.bootstrap.ModuleManagementFactory found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.IMPALA_FACTORY_ATTRIBUTE", e.getMessage());
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
		verify(moduleStateHolder);
	}

	private void replayMocks() {
		replay(servletContext);
		replay(impalaBootstrapFactory);
		replay(pluginSpecBuilder);
		replay(moduleStateHolder);
	}

}
