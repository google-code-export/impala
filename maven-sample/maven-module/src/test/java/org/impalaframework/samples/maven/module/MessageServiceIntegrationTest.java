package org.impalaframework.samples.maven.module;

import junit.framework.TestCase;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.InteractiveTestRunner;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.samples.maven.MessageService;

public class MessageServiceIntegrationTest extends TestCase implements ModuleDefinitionSource {
    
    public static void main(String[] args) {
        System.setProperty("impala."+LocationConstants.MODULE_CLASS_DIR_PROPERTY, "target/classes");
        System.setProperty("impala."+LocationConstants.MODULE_TEST_DIR_PROPERTY, "target/test-classes");
        InteractiveTestRunner.run(MessageServiceIntegrationTest.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        Impala.init(this);
    }

    public void testIntegration() {
        MessageService service = Impala.getModuleBean("module1", "messageService", MessageService.class);
        System.out.println(service.getMessage());
        assertEquals("Hello TSS Europe", service.getMessage());
    }

    public RootModuleDefinition getModuleDefinition() {
        return new TestDefinitionSource("main", "module").getModuleDefinition();
    }
}
