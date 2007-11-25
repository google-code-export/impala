import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.test.AutomatedBuildTests;
import org.impalaframework.test.AutomatedCoreTests;
import org.impalaframework.test.AutomatedInteractiveTests;
import org.impalaframework.web.test.AutomatedWebTests;


public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(AutomatedCoreTests.suite());
		suite.addTest(AutomatedBuildTests.suite());
		suite.addTest(AutomatedInteractiveTests.suite());
		suite.addTest(AutomatedWebTests.suite());
		return suite;
	}
}
