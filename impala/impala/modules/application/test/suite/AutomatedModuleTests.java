package suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.ProjectMessageIntegrationTest;

public class AutomatedModuleTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(ProjectMessageIntegrationTest.class);
		return suite;
	}
}