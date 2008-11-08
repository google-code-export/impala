package org.impalaframework.osgiroot.test;

import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.osgi.test.OsgiIntegrationTest;
import org.osgi.framework.ServiceReference;

public class ReloadTest extends OsgiIntegrationTest {

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