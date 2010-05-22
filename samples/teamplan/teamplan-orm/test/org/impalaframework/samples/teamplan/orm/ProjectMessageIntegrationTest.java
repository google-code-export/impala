package org.impalaframework.samples.teamplan.orm;

import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.interactive.InteractiveTestRunner;

import org.impalaframework.samples.teamplan.main.BaseIntegrationTest;
import org.impalaframework.samples.teamplan.main.MessageService;


public class ProjectMessageIntegrationTest extends BaseIntegrationTest {

    public static void main(String[] args) {
        InteractiveTestRunner.run(ProjectMessageIntegrationTest.class);
    }

    public void testIntegration() {
        MessageService service = Impala.getModuleBean("teamplan-orm", "messageService", MessageService.class);
        System.out.println(service.getMessage());
    }

    public RootModuleDefinition getModuleDefinition() {
        return new TestDefinitionSource("teamplan-main", "teamplan-orm").getModuleDefinition();
    }

}
