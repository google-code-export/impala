package org.impalaframework.osgiservice;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.io.OsgiBundleResource;
import org.springframework.util.FileCopyUtils;

public class ResourceActivator implements BundleActivator {
	public void start(BundleContext bundleContext) throws Exception {
		URL resource = bundleContext.getBundle().getResource("META-INF/main.txt");
		
		if (resource != null) {
			InputStream stream = resource.openStream();
			FileCopyUtils.copyToString(new InputStreamReader(stream));
			System.out.println("Found resource for META-INF/main.txt: " + stream);
		}

		Bundle b = getBundleWithName(bundleContext, "org.impalaframework.osgisample");
		OsgiBundleResource br = new OsgiBundleResource(b, "META-INF/main.txt");
		System.out.println("Found via bundle: " + br.exists());
	}

	public void stop(BundleContext bundleContext) throws Exception {	
	}	
	
	private Bundle getBundleWithName(BundleContext bundleContext, String name) {
		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			if (name.equals(bundle.getSymbolicName())) {
				return bundle;
			}
		}
		return null;
	}
}
