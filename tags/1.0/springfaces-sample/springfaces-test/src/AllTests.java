import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.application.module1.suite.AutomatedModuleTests;
import com.application.web.suite.AutomatedWebTests;
import com.application.main.suite.AutomatedRootTests;

public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(AutomatedRootTests.suite());
		suite.addTest(AutomatedModuleTests.suite());
		suite.addTest(AutomatedWebTests.suite());
		return suite;
	}
}
