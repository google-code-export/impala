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

package org.impalaframework.osgi.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.impalaframework.exception.ExecutionException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.springframework.core.io.Resource;
import org.springframework.osgi.io.OsgiBundleResource;
import org.springframework.osgi.util.OsgiBundleUtils;
import org.springframework.util.Assert;

/**
 * Utility class containing general purpose methods useful for an OSGi environment. 
 * @author Phil Zoio
 */
public abstract class OsgiUtils {
	
	/* ****************** Bundle resource related methods ************** */
	
	/**
	 * Finds resources not just in the host bundle but in any bundle which
	 * contains the locations. Does not guarantee that any resource is loaded
	 * from any particular bundle. Instead, relies on sensible naming
	 * conventions for resources to be found.
	 */
	public static URL[] findResources(BundleContext bundleContext, String[] names) {
		
		//FIXME test
		List<URL> urls = new ArrayList<URL>();
		
		for (int i = 0; i < names.length; i++) {
			URL findResource = findResource(bundleContext, names[i]);
			if (findResource != null) {
				urls.add(findResource);
			}
		}
		return urls.toArray(new URL[0]);
	}

	//FIXME test
	/**
	 * Finds an individual resources not just in the host bundle but in any
	 * bundle which contains the locations. Does not guarantee that any resource
	 * is loaded from any particular bundle. Instead, relies on sensible naming
	 * conventions for resources to be found.
	 */
	public static URL findResource(BundleContext bundleContext, String name) {
		
		URL resource = null;
		//find the resources
		
		//first look in host bundle, then cycle through others
		final Bundle hostBundle = bundleContext.getBundle();
		resource = hostBundle.getResource(name);
		
		if (resource != null) return resource;
		
		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			resource = bundle.getResource(name);
			if (resource != null) {
				return resource;
			}
		}
		return null;
	}
	
	/**
	 * Finds the bundle whose name is equal to the name supplied as a method argument.
	 * Cycles through the bundles accessible to the supplied {@link BundleContext} via 
	 * the method {@link BundleContext#getBundle()}.
	 */
	@SuppressWarnings("unchecked")
	public static Bundle findBundle(BundleContext bundleContext, String name) {
		
		Assert.notNull(bundleContext, "bundleContext cannot be null");
		Assert.notNull(name, "name cannot be null");
		
		String resource = null;
		//find the resources
		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			final Dictionary headers = bundle.getHeaders();
			resource = (String) headers.get(org.osgi.framework.Constants.BUNDLE_NAME);
			if (name.equals(resource)) {
				return bundle;
			}
		}
		return null;
	}
	
	public static Resource[] getBundleResources(Bundle bundle, List<String> resourceNames) {
		//FIXME test
		Resource[] resources = new Resource[resourceNames.size()];
		for (int i = 0; i < resources.length; i++) {
			resources[i] = new OsgiBundleResource(bundle, resourceNames.get(i));
		}
		return resources;
	}

	public static String getBundleLocation(Resource bundleResource) {
		//FIXME test
		String bundleLocation;

		try {
			//based on the original code in org.springframework.osgi.test.AbstractOsgiTests
			bundleLocation = URLDecoder.decode(bundleResource.getURL().toExternalForm(), "UTF8");
		}
		catch (Exception ex) {
			// the URL cannot be created, fall back to the description
			bundleLocation = bundleResource.getDescription();
		}
		return bundleLocation;
	}
	
	/* ****************** Bundle life cycle methods ************** */
	
	public static Bundle installBundle(BundleContext bundleContext, Resource bundleResource) {
		
		//TODO add logging
		//FIXME test
		
		Assert.notNull(bundleResource, "bundleResource cannot be null");
		
		Bundle bundle = null;
		//FIXME check that resource exists
		try {
			final InputStream resource = bundleResource.getInputStream();
			final String bundleLocation = getBundleLocation(bundleResource);
			
			bundle = bundleContext.installBundle(bundleLocation, resource);
			
		} catch (BundleException e) {
			throw new ExecutionException(e);
		} catch (IOException e) {
			throw new ExecutionException(e);
		}
		return bundle;
	}

	public static void startBundle(Bundle bundle) {
		//TODO add logging
		//FIXME test
		if (!OsgiBundleUtils.isFragment(bundle)) {
			try {
				bundle.start();
				
			} catch (BundleException e) {
				throw new ExecutionException("Unable to start bundle '" + bundle.getSymbolicName() + "': " + e.getMessage(), e);
			}
		}
	}

	public static void updateBundle(Bundle bundle, final Resource resource) {
		try {
			bundle.update(resource.getInputStream());
			
		} catch (BundleException e) {
			throw new ExecutionException(e);
		} catch (IOException e) {
			throw new ExecutionException(e);
		}
	}

	public static boolean stopBundle(Bundle bundle) {
		//FIXME test
		try {
			//should we call stop first
			bundle.stop();
			
		} catch (BundleException e) {
			throw new ExecutionException(e);
		}
		return true;
	}
	
}
