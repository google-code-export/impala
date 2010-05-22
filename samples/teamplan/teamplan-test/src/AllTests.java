import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.impalaframework.samples.teamplan.orm.suite.AutomatedModuleTests;
import org.impalaframework.samples.teamplan.web.suite.AutomatedWebTests;
import org.impalaframework.samples.teamplan.main.suite.AutomatedRootTests;

public class AllTests extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(AutomatedRootTests.suite());
        suite.addTest(AutomatedModuleTests.suite());
        suite.addTest(AutomatedWebTests.suite());
        return suite;
    }
}
