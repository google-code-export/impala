package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculatorRegistry;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.modification.StrictPluginModificationCalculator;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;

public class BaseImpalaContextLoaderTest extends TestCase {

	private ServletContext servletContext;
	private ImpalaBootstrapFactory factory;
	private PluginStateManager pluginStateManager;
	private PluginModificationCalculatorRegistry calculatorRegistry;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		factory = createMock(ImpalaBootstrapFactory.class);
		pluginStateManager = createMock(PluginStateManager.class);
		
		calculatorRegistry = new PluginModificationCalculatorRegistry();
		calculatorRegistry.addModificationCalculationType(ModificationCalculationType.STRICT, new StrictPluginModificationCalculator());
	}

	public final void testClose() {
		BaseImpalaContextLoader contextLoader = newContextLoader();

		servletContext.log("Closing plugins and root application context hierarchy");
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(factory);
		expect(factory.getPluginStateManager()).andReturn(pluginStateManager);
		expect(factory.getPluginModificationCalculatorRegistry()).andReturn(calculatorRegistry);
		SimpleParentSpec simpleParentSpec = new SimpleParentSpec("parentSpec");
		expect(pluginStateManager.getParentSpec()).andReturn(simpleParentSpec);
		pluginStateManager.processTransitions(isA(PluginTransitionSet.class));
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
			public PluginSpecBuilder getPluginSpecBuilder(ServletContext servletContext) {
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
