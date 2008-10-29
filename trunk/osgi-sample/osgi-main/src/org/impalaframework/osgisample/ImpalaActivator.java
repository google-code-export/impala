package org.impalaframework.osgisample;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

public class ImpalaActivator implements BundleActivator {

	public void start(BundleContext bundleContext) throws Exception {

		URL[] resource = findResources(bundleContext, new String[] {
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-web-bootstrap.xml" });
		
		if (resource != null) {
			Thread currentThread = Thread.currentThread();
			ClassLoader oldTCCL = currentThread.getContextClassLoader();
			
			try
			{
				ClassLoader classLoader = BundleDelegatingClassLoader.createBundleClassLoaderFor(bundleContext.getBundle());
				currentThread.setContextClassLoader(classLoader);
				
				final OSGiApplicationContext applicationContext = new OSGiApplicationContext(resource);		
				applicationContext.setBundleContext(bundleContext);
				applicationContext.refresh();
			}
			finally {
				currentThread.setContextClassLoader(oldTCCL);
			}
		} else {
			System.out.println("Could not find impala-bootstrap.xml resource");
		}
			
	}

	private URL[] findResources(BundleContext bundleContext, String[] names) {
		List<URL> urls = new ArrayList<URL>();
		for (int i = 0; i < names.length; i++) {
			URL findResource = findResource(bundleContext, names[i]);
			if (findResource != null) {
				urls.add(findResource);
			} else {
				//TODO log
			}
		}
		return urls.toArray(new URL[0]);
	}

	private URL findResource(BundleContext bundleContext, String name) {
		URL resource = null;
		//find the resources
		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			resource = bundle.getResource(name);
			if (resource != null) {
				return resource;
			}
		}
		return null;
	}

	public void stop(BundleContext bundleContext) throws Exception {
	}
}
