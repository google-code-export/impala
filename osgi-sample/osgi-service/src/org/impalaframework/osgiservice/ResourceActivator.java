package org.impalaframework.osgiservice;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.util.FileCopyUtils;

public class ResourceActivator implements BundleActivator {
	public void start(BundleContext bundleContext) throws Exception {
		// TODO Auto-generated method stub
		URL resource = bundleContext.getBundle().getResource("META-INF/main.txt");
		
		if (resource != null) {
			InputStream stream = resource.openStream();
			FileCopyUtils.copyToString(new InputStreamReader(stream));
			System.out.println(stream);
		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
