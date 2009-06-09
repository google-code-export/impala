package com.application.module1;

import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.interactive.InteractiveTestRunner;

import com.application.main.BaseIntegrationTest;
import com.application.main.MessageService;



public class ProjectMessageIntegrationTest extends BaseIntegrationTest {

	public static void main(String[] args) {
		InteractiveTestRunner.run(ProjectMessageIntegrationTest.class);
	}

	public void testIntegration() {
		MessageService service = Impala.getModuleBean("springfaces-module1", "messageService", MessageService.class);
		System.out.println(service.getMessage());
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("springfaces-main", "springfaces-module1").getModuleDefinition();
	}

}