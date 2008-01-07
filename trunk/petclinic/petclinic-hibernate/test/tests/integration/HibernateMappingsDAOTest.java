package tests.integration;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.DynamicContextHolder;
import org.impalaframework.testrun.ImpalaTestRunner;

public class HibernateMappingsDAOTest extends TestCase implements
		ModuleDefinitionSource {

	public static void main(String[] args) {
		System.setProperty("impala.parent.project", "petclinic");
		ImpalaTestRunner.run(HibernateMappingsDAOTest.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		System.setProperty("impala.parent.project", "petclinic");
		DynamicContextHolder.init(this);
	}
	
	public void testMappings() throws Exception {
		System.out.println("Session factory: " + DynamicContextHolder.getBean("sessionFactory", SessionFactory.class));
	}

	public RootModuleDefinition getModuleDefinition() {

		return new SimpleModuleDefinitionSource(
				new String[] { "parent-context.xml" },
				new String[] { "petclinic-hibernate" }).getModuleDefinition();
	}

}
