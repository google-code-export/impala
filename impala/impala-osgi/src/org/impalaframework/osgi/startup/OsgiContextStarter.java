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

package org.impalaframework.osgi.startup;

import java.net.URL;
import java.util.List;

import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContext;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.startup.ContextStarter;
import org.osgi.framework.BundleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

//FIXME test
public class OsgiContextStarter implements ContextStarter, BundleContextAware {
	
	private BundleContext bundleContext;

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public ApplicationContext startContext(List<String> locations) {
		
		URL[] urls = OsgiUtils.findResources(bundleContext, locations
				.toArray(new String[locations.size()]));

		//FIXME check that this is not null

		ImpalaOsgiApplicationContext applicationContext = null;

		Thread currentThread = Thread.currentThread();
		ClassLoader oldTCCL = currentThread.getContextClassLoader();

		try {
			ClassLoader classLoader = BundleDelegatingClassLoader
					.createBundleClassLoaderFor(bundleContext.getBundle());
			currentThread.setContextClassLoader(classLoader);

			applicationContext = new ImpalaOsgiApplicationContext();

			Resource[] resources = new Resource[urls.length];
			for (int i = 0; i < resources.length; i++) {
				resources[i] = new UrlResource(urls[i]);
			}

			applicationContext.setBundleContext(bundleContext);
			applicationContext.refresh();

			return applicationContext;
			
		} finally {
			currentThread.setContextClassLoader(oldTCCL);
		}
	}

}
