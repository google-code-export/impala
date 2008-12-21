package test;


import interfaces.MessageService;

import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.testrun.InteractiveTestRunner;

public class ProjectMessageIntegrationTest extends BaseIntegrationTest {

	public static void main(String[] args) {
		InteractiveTestRunner.run(ProjectMessageIntegrationTest.class);
	}

	public void testIntegration() {
		MessageService service = Impala.getModuleBean("webframeworks-service", "messageService", MessageService.class);
		System.out.println(service.getMessage());
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("webframeworks", "webframeworks-service").getModuleDefinition();
	}

}