import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import tests.integration.HibernateClinicTest;
import tests.integration.HibernateMappingsDAOTest;


public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(HibernateMappingsDAOTest.class);
		suite.addTestSuite(HibernateClinicTest.class);
		return suite;
	}
}
