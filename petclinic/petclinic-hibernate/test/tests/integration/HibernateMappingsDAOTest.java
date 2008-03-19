package tests.integration;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.LocationConstants;
import org.impalaframework.testrun.InteractiveTestRunner;

public class HibernateMappingsDAOTest extends TestCase implements
		ModuleDefinitionSource {

	public static void main(String[] args) {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "petclinic");
		InteractiveTestRunner.run(HibernateMappingsDAOTest.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "petclinic");
		Impala.init(this);
	}
	
	public void testMappings() throws Exception {
		System.out.println("Session factory: " + Impala.getBean("sessionFactory", SessionFactory.class));
	}

	public RootModuleDefinition getModuleDefinition() {

		return new SimpleModuleDefinitionSource("petclinic",
				new String[] { "parent-context.xml" },
				new String[] { "petclinic-hibernate" }).getModuleDefinition();
	}

}
