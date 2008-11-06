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

package org.impalaframework.osgi.extender;

import java.net.URL;
import java.util.Properties;

import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.facade.SimpleOperationsFacade;
import org.impalaframework.module.builder.InternalModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContext;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.PropertyUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

//FIXME test
public class ImpalaActivator implements BundleActivator {

	private InternalOperationsFacade operations;
	
	private static final String[] DEFAULT_LOCATIONS = new String[] {
			"META-INF/impala-bootstrap.xml",
			"META-INF/impala-osgi-bootstrap.xml"
			};

	public void start(BundleContext bundleContext) throws Exception {

		//FIXME use fragment to find bootstrap locations
		String bootstrapLocationsResourceName = "impala.properties";
		URL bootstrapLocationsResource = OsgiUtils.findResource(bundleContext, bootstrapLocationsResourceName);
		
		String[] locations = null;
		
		//FIXME find first
		if (bootstrapLocationsResource != null) {
			//FIXME robustify and test this
			final Properties resourceProperties = PropertyUtils.loadProperties(bootstrapLocationsResource);
			String locationString = resourceProperties.getProperty("bootstrapLocations");
			if (locationString != null) locations = locationString.split(",");			
		}
		
		if (locations == null) {
			locations = DEFAULT_LOCATIONS;
		}
		
		URL[] resources = OsgiUtils.findResources(bundleContext, locations);
		
		Resource[] configResources = new Resource[resources.length];
		for (int i = 0; i < configResources.length; i++) {
			configResources[i] = new UrlResource(resources[i]);
		}
		
		ImpalaOsgiApplicationContext applicationContext = null;
		
		if (resources != null) {
			Thread currentThread = Thread.currentThread();
			ClassLoader oldTCCL = currentThread.getContextClassLoader();
			
			try
			{
				ClassLoader classLoader = BundleDelegatingClassLoader.createBundleClassLoaderFor(bundleContext.getBundle());
				currentThread.setContextClassLoader(classLoader);
				
				applicationContext = new ImpalaOsgiApplicationContext();
				
				applicationContext.setConfigResources(configResources);
				applicationContext.setBundleContext(bundleContext);
				applicationContext.refresh();
			}
			finally {
				currentThread.setContextClassLoader(oldTCCL);
			}
		} else {
			System.out.println("Could not find impala-bootstrap.xml resource");
		}
		
		if (applicationContext != null) {
			
			//FIXME find a way to pick up the RootModuleDefinition if it is available via service registry
			
			ModuleManagementFacade facade = ObjectUtils.cast(applicationContext.getBean("moduleManagementFacade"),
					ModuleManagementFacade.class);
			operations = new SimpleOperationsFacade(facade);
			
			ModuleDefinitionSource moduleDefinitionSource = null;
			
			try {
				//FIXME add error handling for this
				ServiceReference serviceReference = bundleContext.getServiceReference(ModuleDefinitionSource.class.getName());
				
				Object service = bundleContext.getService(serviceReference);
				moduleDefinitionSource = ObjectUtils.cast(service, ModuleDefinitionSource.class);
				
			} catch (RuntimeException e) {
				//FIXME better error messaging
				e.printStackTrace();
			}	
			
			if (moduleDefinitionSource == null) {
			
				//FIXME should do nothing here, rather than load a default set of modules.
				//Alternatively, could look for a set of bundles from the extender's property file.
				
				//TODO this needs to be picked up by a fragment
				moduleDefinitionSource = new InternalModuleDefinitionSource(
						facade.getTypeReaderRegistry(), 
						facade.getModuleLocationResolver(), 
						new String[]{ "osgi-root", "osgi-module1" });
			
			}
			
			operations.init(moduleDefinitionSource);
			bundleContext.registerService(OperationsFacade.class.getName(), operations, null);
		
		}
			
	}

	/**
	 * Unloads Impala modules and shuts down Impala's {@link ApplicationContext}.
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		
		//FIXME test		
		if (operations != null) {		
			operations.unloadRootModule();
			operations.getModuleManagementFacade().close();
		}
	}
}

