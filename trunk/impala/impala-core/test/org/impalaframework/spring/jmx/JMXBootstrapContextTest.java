package org.impalaframework.spring.jmx;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
import org.impalaframework.resolver.PropertyModuleLocationResolver;
import org.impalaframework.util.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JMXBootstrapContextTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private ModuleManagementFactory factory;

	public void setUp() {
		System.setProperty(PropertyModuleLocationResolver.ROOT_PROJECTS_PROPERTY, "impala");
	}

	public void tearDown() {
		try {
			factory.close();
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		System.clearProperty(PropertyModuleLocationResolver.ROOT_PROJECTS_PROPERTY);
	}

	public void testBootstrapContext() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-jmx-bootstrap.xml" ,
				"META-INF/impala-jmx-adaptor-bootstrap.xml"});
		Object bean = context.getBean("moduleManagementFactory");
		factory = ObjectUtils.cast(bean, ModuleManagementFactory.class);
		RootModuleDefinition moduleDefinition = new Provider().getModuleDefinition();

		TransitionSet transitions = factory.getModificationExtractorRegistry()
				.getModificationExtractor(ModificationExtractorType.STICKY).getTransitions(null, moduleDefinition);

		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		moduleStateHolder.processTransitions(transitions);

		ModuleManagementOperations pluginOperations = (ModuleManagementOperations) factory.getBean("pluginOperations");

		assertEquals("Could not find plugin duff", pluginOperations.reloadModule("duff"));
		assertEquals("Successfully reloaded impala-sample-dynamic-plugin1", pluginOperations.reloadModule(plugin1));
	}

	class Provider implements ModuleDefinitionSource {
		ModuleDefinitionSource source = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public RootModuleDefinition getModuleDefinition() {
			return source.getModuleDefinition();
		}
	}
}
