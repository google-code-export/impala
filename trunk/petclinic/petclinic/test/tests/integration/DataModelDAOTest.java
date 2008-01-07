package tests.integration;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.DynamicContextHolder;
import org.impalaframework.testrun.ImpalaTestRunner;

public class DataModelDAOTest extends TestCase implements
		ModuleDefinitionSource {

	public static void main(String[] args) {
		ImpalaTestRunner.run(DataModelDAOTest.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		DynamicContextHolder.getBean("sessionFactory", SessionFactory.class);
	}

	public RootModuleDefinition getModuleDefinition() {

		return new SimpleModuleDefinitionSource(
				new String[] { "parent-context.xml" },
				new String[] { "petstore-hibernate" }).getModuleDefinition();
	}

}
