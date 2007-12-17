package org.impalaframework.module.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleModificationCalculatorRegistry;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.modification.StrictModuleModificationCalculator;
import org.impalaframework.module.spec.ModuleDefinitionSource;
import org.impalaframework.module.spec.SimpleRootModuleDefinition;
import org.impalaframework.module.transition.PluginStateManager;
import org.impalaframework.module.web.BaseImpalaContextLoader;
import org.impalaframework.module.web.WebConstants;

public class BaseImpalaContextLoaderTest extends TestCase {

	private ServletContext servletContext;
	private ModuleManagementSource factory;
	private PluginStateManager pluginStateManager;
	private ModuleModificationCalculatorRegistry calculatorRegistry;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		factory = createMock(ModuleManagementSource.class);
		pluginStateManager = createMock(PluginStateManager.class);
		
		calculatorRegistry = new ModuleModificationCalculatorRegistry();
		calculatorRegistry.addModificationCalculationType(ModificationCalculationType.STRICT, new StrictModuleModificationCalculator());
	}

	public final void testClose() {
		BaseImpalaContextLoader contextLoader = newContextLoader();

		servletContext.log("Closing plugins and root application context hierarchy");
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(factory);
		expect(factory.getPluginStateManager()).andReturn(pluginStateManager);
		expect(factory.getPluginModificationCalculatorRegistry()).andReturn(calculatorRegistry);
		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("parentSpec");
		expect(pluginStateManager.getParentSpec()).andReturn(simpleRootModuleDefinition);
		pluginStateManager.processTransitions(isA(ModuleTransitionSet.class));
		factory.close();

		replayMocks();
		
		contextLoader.closeWebApplicationContext(servletContext);

		verifyMocks();
	}
	
	public final void testCloseParentNull() {
		BaseImpalaContextLoader contextLoader = newContextLoader();

		servletContext.log("Closing plugins and root application context hierarchy");
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(factory);
		expect(factory.getPluginStateManager()).andReturn(pluginStateManager);
		expect(factory.getPluginModificationCalculatorRegistry()).andReturn(calculatorRegistry);
		expect(pluginStateManager.getParentSpec()).andReturn(null);
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
			public ModuleDefinitionSource getPluginSpecBuilder(ServletContext servletContext) {
				return null;
			}
		};
		return contextLoader;
	}

	private void verifyMocks() {
		verify(servletContext);
		verify(factory);
		verify(pluginStateManager);
	}

	private void replayMocks() {
		replay(servletContext);
		replay(factory);
		replay(pluginStateManager);
	}
	

}
