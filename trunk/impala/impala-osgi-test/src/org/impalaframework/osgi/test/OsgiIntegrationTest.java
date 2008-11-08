/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.osgi.test;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.impalaframework.facade.Impala;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.util.ReflectionUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.test.provisioning.ArtifactLocator;
import org.springframework.osgi.util.OsgiBundleUtils;
import org.springframework.osgi.util.OsgiStringUtils;
import org.springframework.util.Assert;

/**
 * Extends {@link AbstractConfigurableBundleCreatorTests} to support working with Impala in OSGi.
 * @author Phil Zoio
 */
public abstract class OsgiIntegrationTest extends AbstractConfigurableBundleCreatorTests implements ModuleDefinitionSource {

	private ArtifactLocator locator;

	public OsgiIntegrationTest() {
		super();
		locator = new RepositoryArtifactLocator();
	}
	
	/* ********************** Test bundle names ********************* */
	
	@Override
	protected Resource[] getTestBundles() {
		File osgiDirectory = new File("../osgi-repository/osgi");
		File[] thirdPartyBundles = osgiDirectory.listFiles(new BundleFileFilter());
		
		File mainDirectory = new File("../osgi-repository/main");
		File[] mainBundles = mainDirectory.listFiles(new BundleFileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (!super.accept(pathname)) return false;
				if (pathname.getName().contains("build")) return false;
				if (pathname.getName().contains("jmx")) return false;
				if (pathname.getName().contains("extender")) return false;
				if (!pathname.getName().contains("impala")) return false;
				
				return true;
			}
		});
		
		List<Resource> resources = new ArrayList<Resource>();
		addResources(resources, thirdPartyBundles);
		addResources(resources, mainBundles); 
		return resources.toArray(new Resource[0]);
	}
	
	protected void postProcessBundleContext(BundleContext context) throws Exception {
		Impala.init();
		RootModuleDefinition definition = getModuleDefinition();
		
		ServiceReference serviceReference = context.getServiceReference(ModuleDefinitionSource.class.getName());
		
		Object service = context.getService(serviceReference);
		System.out.println(service);
		
		Method method = ReflectionUtils.findMethod(service.getClass(), "inject", new Class[]{Object.class});
		ReflectionUtils.invokeMethod(method, service, new Object[]{definition});
		
		//now fire up extender bundle 
		
		File distDirectory = new File("../osgi-repository/dist");
		File[] distBundles = distDirectory.listFiles(new FileFilter(){

			public boolean accept(File pathname) {
				if (pathname.getName().contains("sources")) return false;
				return pathname.getName().contains("extender");
			} 
			
		});
		
		File mainDirectory = new File("../osgi-repository/main");
		File[] mainBundles = mainDirectory.listFiles(new FileFilter(){

			public boolean accept(File pathname) {
				if (pathname.getName().contains("sources")) return false;
				return pathname.getName().contains("extender");
			} 
			
		});
		
		List<Resource> arrayList = new ArrayList<Resource>();
		addResources(arrayList, distBundles);
		Resource[] addResources = addResources(arrayList, mainBundles);
		
		for (Resource resource : addResources) {
			Bundle bundle = installBundle(context, resource);
			OsgiUtils.startBundle(bundle);
		}
		
		super.postProcessBundleContext(context);
	}
	
	
	/**
	 * Installs an OSGi bundle from the given location.
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 */
	protected Bundle installBundle(BundleContext platformContext, Resource location) throws Exception {
		Assert.notNull(platformContext);
		Assert.notNull(location);
		if (logger.isDebugEnabled())
			logger.debug("Installing bundle from location " + location.getDescription());

		String bundleLocation;

		try {
			bundleLocation = URLDecoder.decode(location.getURL().toExternalForm(), "UTF8");
		}
		catch (Exception ex) {
			// the URL cannot be created, fall back to the description
			bundleLocation = location.getDescription();
		}

		return platformContext.installBundle(bundleLocation, location.getInputStream());
	}

	/**
	 * Starts a bundle and prints a nice logging message in case of failure.
	 * 
	 * @param bundle
	 * @return
	 * @throws BundleException
	 */
	protected void startBundle(Bundle bundle) throws BundleException {
		boolean debug = logger.isDebugEnabled();
		String info = "[" + OsgiStringUtils.nullSafeNameAndSymName(bundle) + "|" + bundle.getLocation() + "]";

		if (!OsgiBundleUtils.isFragment(bundle)) {
			if (debug)
				logger.debug("Starting " + info);
			try {
				bundle.start();
			}
			catch (BundleException ex) {
				logger.error("cannot start bundle " + info, ex);
				throw ex;
			}
		}

		else if (debug)
			logger.debug(info + " is a fragment; start not invoked");
	}
	
	/* ********************** Helper methods ********************* */

	private Resource[] addResources(List<Resource> resources, File[] listFiles) {
		for (int i = 0; i < listFiles.length; i++) {
			resources.add(new FileSystemResource(listFiles[i]));
		}
		
		return resources.toArray(new Resource[0]);
	}

	protected String[] getTestFrameworkBundlesNames() {
		String[] testFrameworkBundlesNames = super.getTestFrameworkBundlesNames();
		for (int i = 0; i < testFrameworkBundlesNames.length; i++) {
			String bundle = testFrameworkBundlesNames[i];
			System.out.println(bundle);
			if (bundle.equals("org.springframework.osgi,log4j.osgi,1.2.15-SNAPSHOT")) {
				bundle = bundle.replace("log4j.osgi,1.2.15-SNAPSHOT", "com.springsource.org.apache.log4j,1.2.15");
				testFrameworkBundlesNames[i] = bundle;
			}
			
		}
		return testFrameworkBundlesNames;
	}
	
	protected ArtifactLocator getLocator() {
		return locator;
	}	
}

class BundleFileFilter implements FileFilter {
	public boolean accept(File pathname) {
		if (pathname.getName().contains("sources")) return false;
		if (!pathname.getName().endsWith(".jar")) return false;
		return true;
	}
}

class RepositoryArtifactLocator implements ArtifactLocator {

	public Resource locateArtifact(String group, String id, String version) {
		String directory = "../osgi-repository/osgi/";
		FileSystemResource resource = findBundleResource(directory, id, version);
		return resource;
	}

	private FileSystemResource findBundleResource(String directory, String id,
			String version) {
		FileSystemResource resource = new FileSystemResource(directory + id + "-" + version + ".jar");
		System.out.println(resource + ": " + resource.exists());
		return resource;
	}

	public Resource locateArtifact(String group, String id, String version,
			String type) {
		String directory = "../osgi-repository/osgi/";
		FileSystemResource resource = new FileSystemResource(directory + id + "-" + version + "-" + type + ".jar");
		System.out.println(resource + ": " + resource.exists());
		return resource;
	}
	
}
