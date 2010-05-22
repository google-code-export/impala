package samples.teamplan.orm.suite;

import samples.teamplan.orm.ProjectMessageIntegrationTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AutomatedModuleTests extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ProjectMessageIntegrationTest.class);
        return suite;
    }
}
