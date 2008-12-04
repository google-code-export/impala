package org.impalaframework.osgiroot.test;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.osgi.test.OsgiIntegrationTest;
import org.osgi.framework.ServiceReference;

public class ReloadTest extends OsgiIntegrationTest {

	public ReloadTest() {
		super();
		//FIXME figure out where to put these
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, "../");
		System.setProperty("application.version", "1.0");
		System.setProperty("impala.module.class.dir", "target/classes");
	}
	
	public void testOsgiEnvironment() throws Exception {
		System.out.println("Starting reload of --------------------- ");
		
		ServiceReference serviceReference = bundleContext.getServiceReference(OperationsFacade.class.getName());
		OperationsFacade facade = (OperationsFacade) bundleContext.getService(serviceReference);
		facade.reload("osgi-root");
		
		System.out.println("Finished reloading module --------------------- ");
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("osgi-root", "osgi-module1").getModuleDefinition();
	}
	
}