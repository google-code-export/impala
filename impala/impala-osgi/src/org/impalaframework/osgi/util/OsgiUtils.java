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

import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.util.ObjectUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.core.io.Resource;
import org.springframework.osgi.io.OsgiBundleResource;

/**
 * Utility class containing general purpose methods useful for an OSGi environment. 
 * @author Phil Zoio
 */
public class OsgiUtils {
	
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
			} else {
				//TODO log
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
	
	/**
	 * Returns the Impala {@link ModuleManagementFacade} from the OSGi service registry.
	 * Will be null if Impala has not been initialised, typically via an Impala {@link BundleActivator} instance.
	 */
	public static ModuleManagementFacade getManagementFacade(BundleContext context) {
		//FIXME add test
		InternalOperationsFacade facade = getOperationsFacade(context);
		return facade.getModuleManagementFacade();
	}

	public static InternalOperationsFacade getOperationsFacade(BundleContext context) {
		//FIXME add test
		ServiceReference serviceReference = context.getServiceReference(OperationsFacade.class.getName());
		InternalOperationsFacade facade = ObjectUtils.cast(context.getService(serviceReference), InternalOperationsFacade.class);
		return facade;
	}
	
	public static Resource[] getBundleResources(Bundle bundle, List<String> resourceNames) {
		//FIXME test
		Resource[] resources = new Resource[resourceNames.size()];
		for (int i = 0; i < resources.length; i++) {
			resources[i] = new OsgiBundleResource(bundle, resourceNames.get(i));
		}
		return resources;
	}
	
}
