package @project.package.name@.@module.project.name@;

import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.interactive.InteractiveTestRunner;

import @project.package.name@.@main.project.name@.BaseIntegrationTest;
import @project.package.name@.@main.project.name@.MessageService;



public class ProjectMessageIntegrationTest extends BaseIntegrationTest {

	public static void main(String[] args) {
		InteractiveTestRunner.run(ProjectMessageIntegrationTest.class);
	}

	public void testIntegration() {
		MessageService service = Impala.getModuleBean("@project.prefix@@module.project.name@", "messageService", MessageService.class);
		System.out.println(service.getMessage());
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("@project.prefix@@main.project.name@", "@project.prefix@@module.project.name@").getModuleDefinition();
	}

}