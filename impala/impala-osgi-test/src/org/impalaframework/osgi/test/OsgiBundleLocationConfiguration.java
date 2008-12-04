package org.impalaframework.osgi.test;

import org.springframework.core.io.Resource;
import org.springframework.osgi.test.provisioning.ArtifactLocator;

public class OsgiBundleLocationConfiguration {

	public ArtifactLocator getArtifactLocator() {
	    return new RepositoryArtifactLocator("../osgi-repository", new String[] {"osgi"});
	}
	
	public Resource[] getTestBundleLocations() {
		ConfigurableFileFilter fileFilter = new ConfigurableFileFilter(
				"osgi: *; main: impala", //include all of osgi plus impala files in main
				"main: build,extender,jmx"); //exclude build, extender and jmx in main
		FileFetcher fileFetcher = new FileFetcher("../osgi-repository", new String[] {"osgi", "main"});
		return fileFetcher.getResources(fileFilter).toArray(new Resource[0]);
	}
	
	public Resource[] getExtenderBundleLocations() {
		//now fire up extender bundle
		ConfigurableFileFilter fileFilter = new ConfigurableFileFilter(
				"dist:extender;main:extender", //include extender in dist
				null); 
		FileFetcher fileFetcher = new FileFetcher("../osgi-repository", new String[] {"dist", "main"});
		return fileFetcher.getResources(fileFilter).toArray(new Resource[0]);
	}
	
}
