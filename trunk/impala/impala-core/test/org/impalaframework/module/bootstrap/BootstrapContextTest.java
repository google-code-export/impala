package org.impalaframework.module.bootstrap;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.module.builder.SimplePluginSpecBuilder;
import org.impalaframework.module.loader.PluginLoaderRegistry;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.PluginModificationCalculatorRegistry;
import org.impalaframework.module.modification.PluginTransitionSet;
import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpecProvider;
import org.impalaframework.module.spec.PluginTypes;
import org.impalaframework.module.transition.PluginStateManager;
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
				"META-INF/impala-bootstrap.xml");
		PluginModificationCalculatorRegistry calculatorRegistry = (PluginModificationCalculatorRegistry) context
				.getBean("pluginModificationCalculatorRegistry");
		PluginLoaderRegistry registry = (PluginLoaderRegistry) context.getBean("pluginLoaderRegistry");
		
		assertNotNull(registry.getPluginLoader(PluginTypes.ROOT));
		assertNotNull(registry.getPluginLoader(PluginTypes.APPLICATION));
		assertNotNull(registry.getPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS));

		PluginStateManager pluginStateManager = (PluginStateManager) context.getBean("pluginStateManager");

		ParentSpec pluginSpec = new Provider().getPluginSpec();
		PluginTransitionSet transitions = calculatorRegistry.getPluginModificationCalculator(ModificationCalculationType.STRICT).getTransitions(null, pluginSpec);
		pluginStateManager.processTransitions(transitions);

		ConfigurableApplicationContext parentContext = pluginStateManager.getParentContext();
		FileMonitor bean = (FileMonitor) parentContext.getBean("bean1");
		bean.lastModified((File) null);
	}

	class Provider implements PluginSpecProvider {
		PluginSpecProvider spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public ParentSpec getPluginSpec() {
			return spec.getPluginSpec();
		}
	}
}
