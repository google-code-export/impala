package test;


import interfaces.MessageService;

import org.impalaframework.facade.Impala;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.InteractiveTestRunner;

import test.BaseIntegrationTest;

public class ProjectMessageIntegrationTest extends BaseIntegrationTest {

	public static void main(String[] args) {
		InteractiveTestRunner.run(ProjectMessageIntegrationTest.class);
	}

	public void testIntegration() {
		MessageService service = Impala.getModuleBean("@module.project.name@", "messageService", MessageService.class);
		System.out.println(service.getMessage());
	}

	public RootModuleDefinition getModuleDefinition() {
		return new SimpleModuleDefinitionSource("wineorder",
				new String[] { "parent-context.xml" }, new String[] {"@module.project.name@"}).getModuleDefinition();
	}

}