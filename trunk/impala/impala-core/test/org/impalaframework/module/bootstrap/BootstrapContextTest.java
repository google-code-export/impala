package org.impalaframework.module.bootstrap;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.loader.PluginLoaderRegistry;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.PluginModificationCalculatorRegistry;
import org.impalaframework.module.modification.PluginTransitionSet;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;
import org.impalaframework.module.spec.ModuleTypes;
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
		
		assertNotNull(registry.getPluginLoader(ModuleTypes.ROOT));
		assertNotNull(registry.getPluginLoader(ModuleTypes.APPLICATION));
		assertNotNull(registry.getPluginLoader(ModuleTypes.APPLICATION_WITH_BEANSETS));

		PluginStateManager pluginStateManager = (PluginStateManager) context.getBean("pluginStateManager");

		RootModuleDefinition pluginSpec = new Provider().getModuleDefintion();
		PluginTransitionSet transitions = calculatorRegistry.getPluginModificationCalculator(ModificationCalculationType.STRICT).getTransitions(null, pluginSpec);
		pluginStateManager.processTransitions(transitions);

		ConfigurableApplicationContext parentContext = pluginStateManager.getParentContext();
		FileMonitor bean = (FileMonitor) parentContext.getBean("bean1");
		bean.lastModified((File) null);
	}

	class Provider implements ModuleDefinitionSource {
		ModuleDefinitionSource spec = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public RootModuleDefinition getModuleDefintion() {
			return spec.getModuleDefintion();
		}
	}
}
