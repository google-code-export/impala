package tests.integration;

import junit.framework.TestCase;

import org.hibernate.SessionFactory;
import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.InteractiveTestRunner;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.source.SimpleModuleDefinitionSource;

public class HibernateMappingsDAOTest extends TestCase implements
        ModuleDefinitionSource {

    public static void main(String[] args) {
        InteractiveTestRunner.run(HibernateMappingsDAOTest.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        Impala.init(this);
    }
    
    public void testMappings() throws Exception {
        System.out.println("Session factory: " + Impala.getBean("sessionFactory", SessionFactory.class));
    }

    public RootModuleDefinition getModuleDefinition() {

        return new SimpleModuleDefinitionSource("petclinic-main",
                new String[] { "petclinic-context.xml" },
                new String[] { "petclinic-hibernate" }).getModuleDefinition();
    }

}
