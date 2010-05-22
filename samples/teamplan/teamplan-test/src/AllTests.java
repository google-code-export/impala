import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import samples.teamplan.main.suite.AutomatedRootTests;
import samples.teamplan.orm.suite.AutomatedModuleTests;
import samples.teamplan.web.suite.AutomatedWebTests;

public class AllTests extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(AutomatedRootTests.suite());
        suite.addTest(AutomatedModuleTests.suite());
        suite.addTest(AutomatedWebTests.suite());
        return suite;
    }
}
