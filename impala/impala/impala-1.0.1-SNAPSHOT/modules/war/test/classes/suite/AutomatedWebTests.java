package @project.package.name@.@web.project.name@.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AutomatedWebTests extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite();
        return suite;
    }
}
