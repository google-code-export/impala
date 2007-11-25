package org.impalaframework.plugin.bootstrap;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.modification.StrictPluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BootstrapContextTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	public void setUp() {
		System.setProperty("impala.parent.project", "impala");
	}

	public void tearDown() {
		System.clearProperty("impala.parent.project");
	}

	public void testBootstrapContext() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"org/impalaframework/plugin/bootstrap/impala-bootstrap.xml");
		StrictPluginModificationCalculator calculator = (StrictPluginModificationCalculator) context
				.getBean("pluginModificationCalculator");
		PluginLoaderRegistry registry = (PluginLoaderRegistry) context.getBean("pluginLoaderRegistry");
		
		assertNotNull(registry.getPluginLoader(PluginTypes.ROOT));
		assertNotNull(registry.getPluginLoader(PluginTypes.APPLICATION));
		assertNotNull(registry.getPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS));

		PluginStateManager pluginStateManager = (PluginStateManager) context.getBean("pluginStateManager");

		ParentSpec pluginSpec = new Provider().getPluginSpec();
		PluginTransitionSet transitions = calculator.getTransitions(null, pluginSpec);
		pluginStateManager.processTransitions(transitions);

		ConfigurableApplicationContext parentContext = pluginStateManager.getParentContext();
		FileMonitor bean = (FileMonitor) parentContext.getBean("bean1");
		bean.lastModified((File) null);
	}

	class Provider implements PluginSpecProvider {
		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public ParentSpec getPluginSpec() {
			return spec.getParentSpec();
		}
	}
}
