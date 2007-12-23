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
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.impalaframework.web.WebConstants;

public class WebModuleReloaderTest extends TestCase {

	private ServletContext servletContext;

	private WebModuleReloader reloader;

	private ModuleManagementFactory impalaBootstrapFactory;
	
	private ModuleOperationRegistry moduleOperationRegistry;

	private ModuleOperation moduleOperation;

	private ModuleDefinitionSource moduleDefinitionSource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		impalaBootstrapFactory = createMock(ModuleManagementFactory.class);
		moduleDefinitionSource = createMock(ModuleDefinitionSource.class);

		reloader = new WebModuleReloader();
		reloader.setServletContext(servletContext);

		moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
		moduleOperation = createMock(ModuleOperation.class);
	}

	public final void testReloadPlugins() {
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(impalaBootstrapFactory);
		expect(servletContext.getAttribute(WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE)).andReturn(moduleDefinitionSource);
		
		expect(impalaBootstrapFactory.getModuleOperationRegistry()).andReturn(moduleOperationRegistry);
		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadRootModuleOperation)).andReturn(moduleOperation);
		expect(moduleOperation.execute(isA(ModuleOperationInput.class))).andReturn(ModuleOperationResult.TRUE);
	
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
		expect(servletContext.getAttribute(WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE)).andReturn(null);

		replayMocks();
		try {
			reloader.reloadPlugins();
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("No instance of org.impalaframework.module.definition.ModuleDefinitionSource found. Your context loader needs to be configured to create an instance of this class and attach it to the ServletContext using the attribue WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE", e.getMessage());
		}
		verifyMocks();
	}

	private void verifyMocks() {
		verify(servletContext);
		verify(moduleDefinitionSource);
		verify(impalaBootstrapFactory);
		verify(moduleOperationRegistry);
		verify(moduleOperation);
	}

	private void replayMocks() {
		replay(servletContext);
		replay(moduleDefinitionSource);
		replay(impalaBootstrapFactory);
		replay(moduleOperationRegistry);
		replay(moduleOperation);
	}

}
