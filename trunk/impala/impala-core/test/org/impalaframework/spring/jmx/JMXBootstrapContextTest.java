package org.impalaframework.spring.jmx;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementSource;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JMXBootstrapContextTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private ClassPathXmlApplicationContext context;

	public void setUp() {
		System.setProperty("impala.parent.project", "impala");
	}

	public void tearDown() {
		try {
			context.close();
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		System.clearProperty("impala.parent.project");
	}

	public void testBootstrapContext() throws Exception {
		context = new ClassPathXmlApplicationContext(new String[] {
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-jmx-bootstrap.xml" ,
				"META-INF/impala-jmx-adaptor-bootstrap.xml"});
		BeanFactoryModuleManagementSource factory = new BeanFactoryModuleManagementSource(context);

		RootModuleDefinition pluginSpec = new Provider().getModuleDefinition();

		TransitionSet transitions = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationExtractorType.STICKY).getTransitions(null, pluginSpec);

		ModuleStateHolder moduleStateHolder = factory.getPluginStateManager();
		moduleStateHolder.processTransitions(transitions);

		ModuleManagementOperations pluginOperations = (ModuleManagementOperations) factory.getBean("pluginOperations");

		assertEquals("Could not find plugin duff", pluginOperations.reloadPlugin("duff"));
		assertEquals("Successfully reloaded impala-sample-dynamic-plugin1", pluginOperations.reloadPlugin(plugin1));
	}

	class Provider implements ModuleDefinitionSource {
		ModuleDefinitionSource spec = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public RootModuleDefinition getModuleDefinition() {
			return spec.getModuleDefinition();
		}
	}
}
