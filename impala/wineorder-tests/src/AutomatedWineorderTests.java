

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.facade.FacadeConstants;
import org.impalaframework.facade.SuiteOperationFacade;

import tests.AlternativeWineMerchantTest;
import tests.InitialIntegrationTest;
import tests.WineDAOTest;
import tests.WineMerchantTest;
import tests.WineProjectMerchantTest;

public class AutomatedWineorderTests {

	public static Test suite() {
		System.setProperty(FacadeConstants.FACADE_CLASS_NAME, SuiteOperationFacade.class.getName());
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
