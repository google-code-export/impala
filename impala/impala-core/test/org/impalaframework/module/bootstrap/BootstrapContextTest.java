package org.impalaframework.module.bootstrap;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.TransitionSet;
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
		ModificationExtractorRegistry calculatorRegistry = (ModificationExtractorRegistry) context
				.getBean("pluginModificationCalculatorRegistry");
		ModuleLoaderRegistry registry = (ModuleLoaderRegistry) context.getBean("pluginLoaderRegistry");
		
		assertNotNull(registry.getPluginLoader(ModuleTypes.ROOT));
		assertNotNull(registry.getPluginLoader(ModuleTypes.APPLICATION));
		assertNotNull(registry.getPluginLoader(ModuleTypes.APPLICATION_WITH_BEANSETS));

		ModuleStateHolder moduleStateHolder = (ModuleStateHolder) context.getBean("pluginStateManager");

		RootModuleDefinition pluginSpec = new Provider().getModuleDefinition();
		TransitionSet transitions = calculatorRegistry.getPluginModificationCalculator(ModificationExtractorType.STRICT).getTransitions(null, pluginSpec);
		moduleStateHolder.processTransitions(transitions);

		ConfigurableApplicationContext parentContext = moduleStateHolder.getParentContext();
		FileMonitor bean = (FileMonitor) parentContext.getBean("bean1");
		bean.lastModified((File) null);
	}

	class Provider implements ModuleDefinitionSource {
		ModuleDefinitionSource spec = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public RootModuleDefinition getModuleDefinition() {
			return spec.getModuleDefinition();
		}
	}
}
