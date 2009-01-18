import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import suite.AutomatedModuleTests;
import suite.AutomatedWebTests;
import suite.AutomatedRootTests;

public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(AutomatedRootTests.suite());
		suite.addTest(AutomatedModuleTests.suite());
		suite.addTest(AutomatedWebTests.suite());
		return suite;
	}
}
