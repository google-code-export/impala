package test;

import java.io.Serializable;

import org.hibernate.Hibernate;
import org.impalaframework.facade.Impala;
import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.InteractiveTestRunner;
import test.BaseDataTest;

public class MappingIntegrationTest  //extends BaseDataTest
		extends BaseIntegrationTest {

	public static void main(String[] args) {
		InteractiveTestRunner.run(MappingIntegrationTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		//DAO dao = Impala.getBean("dao", DAO.class);
	}
	
	public void testDAO() throws Exception {
		//insert your test code here
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("@main.project.name@", "@module.project.name@").getModuleDefinition();
	}

}