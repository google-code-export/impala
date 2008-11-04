package org.impalaframework.osgisample.test;

import org.impalaframework.osgi.test.OsgiIntegrationTest;
import org.osgi.framework.Bundle;
import org.springframework.osgi.util.OsgiStringUtils;

public class TestOSGiContext extends OsgiIntegrationTest {

	public void testOsgiEnvironment() throws Exception {
		Bundle[] bundles = bundleContext.getBundles();
		for (int i = 0; i < bundles.length; i++) {
			System.out.print(OsgiStringUtils.nullSafeName(bundles[i]));
			System.out.print(" (" + bundles[i].getSymbolicName() + ")");
			if (i + 1 < bundles.length) System.out.print(", ");
		}
		System.out.println();
		Thread.sleep(2000);
	}
	
}