package org.impalaframework.osgiroot.test;

import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.osgi.test.OsgiIntegrationTest;
import org.osgi.framework.Bundle;
import org.springframework.osgi.util.OsgiStringUtils;

public class OsgiContextTest extends OsgiIntegrationTest {

	public void testOsgiEnvironment() throws Exception {
		Bundle[] bundles = bundleContext.getBundles();
		
		System.out.println("Bundles loaded <------------------- ");
		for (int i = 0; i < bundles.length; i++) {
			System.out.print(OsgiStringUtils.nullSafeName(bundles[i]));
			System.out.println(" (" + bundles[i].getSymbolicName() + ")");
		}
		System.out.println("------------------->");
		Thread.sleep(2000);
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("osgi-root", "osgi-module1").getModuleDefinition();
	}
	
}