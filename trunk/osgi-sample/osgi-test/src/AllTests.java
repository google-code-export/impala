import org.impalaframework.osgiroot.test.MessageServiceTests;
import org.impalaframework.osgiroot.test.TestOSGiContext;
import org.impalaframework.osgiroot.test.TestReload;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite(); 
		suite.addTestSuite(MessageServiceTests.class);
		suite.addTestSuite(TestOSGiContext.class);
		suite.addTestSuite(TestReload.class);
		return suite;
	}
}
