import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.impalaframework.urlmapping.root.suite.AutomatedRootTests;
import org.impalaframework.urlmapping.web.suite.AutomatedWebTests;

public class AllTests extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(AutomatedRootTests.suite());
        suite.addTest(AutomatedWebTests.suite());
        return suite;
    }
}
