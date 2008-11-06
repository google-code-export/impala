package org.impalaframework.osgiroot;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ResourceActivator implements BundleActivator {
	
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Started bundle " + bundleContext.getBundle().getSymbolicName());
	}

	public void stop(BundleContext bundleContext) throws Exception {	
		System.out.println("Stopping bundle " + bundleContext.getBundle().getSymbolicName());
	}
	
}
