import org.impalaframework.osgiroot.test.MessageServiceTest;
import org.impalaframework.osgiroot.test.OsgiContextTest;
import org.impalaframework.osgiroot.test.ReloadTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite(); 
		suite.addTestSuite(MessageServiceTest.class);
		suite.addTestSuite(OsgiContextTest.class);
		suite.addTestSuite(ReloadTest.class);
		return suite;
	}
}
