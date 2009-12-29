import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import @project.package.name@.@module.project.name@.suite.AutomatedModuleTests;
import @project.package.name@.@web.project.name@.suite.AutomatedWebTests;
import @project.package.name@.@main.project.name@.suite.AutomatedRootTests;

public class AllTests extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(AutomatedRootTests.suite());
        suite.addTest(AutomatedModuleTests.suite());
        suite.addTest(AutomatedWebTests.suite());
        return suite;
    }
}
