import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.MessageIntegrationTest;
import test.ProjectMessageIntegrationTest;


public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(MessageIntegrationTest.class);
		suite.addTestSuite(ProjectMessageIntegrationTest.class);
		return suite;
	}
}
