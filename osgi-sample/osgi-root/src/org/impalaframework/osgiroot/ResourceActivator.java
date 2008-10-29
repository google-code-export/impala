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
	
	/*
	private Bundle getBundleWithName(BundleContext bundleContext, String name) {
		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			if (name.equals(bundle.getSymbolicName())) {
				return bundle;
			}
		}
		return null;
	}*/
}
