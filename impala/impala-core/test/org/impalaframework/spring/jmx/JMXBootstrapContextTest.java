package org.impalaframework.spring.jmx;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementSource;
import org.impalaframework.module.builder.SimplePluginSpecBuilder;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.PluginTransitionSet;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;
import org.impalaframework.module.transition.PluginStateManager;
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

		RootModuleDefinition pluginSpec = new Provider().getPluginSpec();

		PluginTransitionSet transitions = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationCalculationType.STICKY).getTransitions(null, pluginSpec);

		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		pluginStateManager.processTransitions(transitions);

		JMXPluginOperations pluginOperations = (JMXPluginOperations) factory.getBean("pluginOperations");

		assertEquals("Could not find plugin duff", pluginOperations.reloadPlugin("duff"));
		assertEquals("Successfully reloaded impala-sample-dynamic-plugin1", pluginOperations.reloadPlugin(plugin1));
	}

	class Provider implements ModuleDefinitionSource {
		ModuleDefinitionSource spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public RootModuleDefinition getPluginSpec() {
			return spec.getPluginSpec();
		}
	}
}
