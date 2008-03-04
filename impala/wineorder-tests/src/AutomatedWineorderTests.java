

import org.impalaframework.resolver.LocationConstants;

import junit.framework.Test;
import junit.framework.TestSuite;
import tests.AlternativeWineMerchantTest;
import tests.InitialIntegrationTest;
import tests.WineDAOTest;
import tests.WineMerchantTest;
import tests.WineProjectMerchantTest;

public class AutomatedWineorderTests {

	public static Test suite() {
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, "../wineorder-sample/");
		
		TestSuite suite = new TestSuite();
		//note some of these tests are repeated to simulated a larger test suite
		//and the effects of reloading/unloading
		suite.addTestSuite(InitialIntegrationTest.class);
		suite.addTestSuite(InProjectWineDAOTest.class);
		suite.addTestSuite(WineDAOTest.class);
		suite.addTestSuite(WineMerchantTest.class);
		suite.addTestSuite(AlternativeWineMerchantTest.class);
		suite.addTestSuite(InitialIntegrationTest.class);
		suite.addTestSuite(WineMerchantTest.class);
		suite.addTestSuite(WineProjectMerchantTest.class);
		suite.addTestSuite(WineDAOTest.class);
		return suite;
	}
}
