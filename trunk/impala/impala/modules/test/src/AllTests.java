import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import classes.suite.AutomatedModuleTests;
import classes.suite.AutomatedWebTests;
import classes.suite.AutomatedRootTests;

public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(AutomatedRootTests.suite());
		suite.addTest(AutomatedModuleTests.suite());
		suite.addTest(AutomatedWebTests.suite());
		return suite;
	}
}
