package classes;

import org.impalaframework.interactive.InteractiveTestRunner;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;

import classes.BaseIntegrationTest;

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
		return new TestDefinitionSource("main", "hibernate").getModuleDefinition();
	}

}