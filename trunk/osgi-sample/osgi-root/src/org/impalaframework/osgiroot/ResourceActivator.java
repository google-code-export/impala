package org.impalaframework.osgiroot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ResourceActivator implements BundleActivator {
	
	private static Log logger = LogFactory.getLog(ResourceActivator.class);

	public void start(BundleContext bundleContext) throws Exception {
		logger.info("Started bundle " + bundleContext.getBundle().getSymbolicName());
	}

	public void stop(BundleContext bundleContext) throws Exception {	
		logger.info("Stopping bundle " + bundleContext.getBundle().getSymbolicName());
	}
	
}
