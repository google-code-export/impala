package org.impalaframework.web.loader;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.impalaframework.web.WebConstants;

public class BaseImpalaContextLoaderTest extends TestCase {

	private ServletContext servletContext;
	private ModuleManagementFactory factory;
	private ModuleOperationRegistry moduleOperationRegistry;
	private ModuleOperation moduleOperation;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		factory = createMock(ModuleManagementFactory.class);
		moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
		moduleOperation = createMock(ModuleOperation.class);}

	public final void testClose() {
		BaseImpalaContextLoader contextLoader = newContextLoader();

		servletContext.log("Closing plugins and root application context hierarchy");
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(factory);
		
		expect(factory.getModuleOperationRegistry()).andReturn(moduleOperationRegistry);
		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.CloseRootModuleOperation)).andReturn(moduleOperation);
		expect(moduleOperation.execute(null)).andReturn(ModuleOperationResult.TRUE);
		
		factory.close();

		replayMocks();
		
		contextLoader.closeWebApplicationContext(servletContext);

		verifyMocks();
	}
	
	public final void testCloseParentNull() {
		BaseImpalaContextLoader contextLoader = newContextLoader();

		servletContext.log("Closing plugins and root application context hierarchy");
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(factory);
		
		expect(factory.getModuleOperationRegistry()).andReturn(moduleOperationRegistry);
		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.CloseRootModuleOperation)).andReturn(moduleOperation);
		expect(moduleOperation.execute(null)).andReturn(ModuleOperationResult.FALSE);

		servletContext.log("Closing Spring root WebApplicationContext");
		factory.close();

		replayMocks();
		
		contextLoader.closeWebApplicationContext(servletContext);

		verifyMocks();
	}
	
	public final void testFactoryNull() {
		BaseImpalaContextLoader contextLoader = newContextLoader();

		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(null);

		replayMocks();
		
		contextLoader.closeWebApplicationContext(servletContext);

		verifyMocks();
	}

	private BaseImpalaContextLoader newContextLoader() {
		BaseImpalaContextLoader contextLoader = new BaseImpalaContextLoader() {
			@Override
			public ModuleDefinitionSource getModuleDefinitionSource(ServletContext servletContext) {
				return null;
			}
		};
		return contextLoader;
	}

	private void verifyMocks() {
		verify(servletContext);
		verify(factory);
		verify(moduleOperationRegistry);
		verify(moduleOperation);
	}

	private void replayMocks() {
		replay(servletContext);
		replay(factory);
		replay(moduleOperationRegistry);
		replay(moduleOperation);
	}
	

}
