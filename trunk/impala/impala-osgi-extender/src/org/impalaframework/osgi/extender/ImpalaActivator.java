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
import java.util.Arrays;
import java.util.Properties;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.facade.SimpleOperationsFacade;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContext;
import org.impalaframework.osgi.startup.OsgiContextStarter;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.PropertyUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.context.ApplicationContext;

//FIXME test
//TODO add logging
public class ImpalaActivator implements BundleActivator {

	private InternalOperationsFacade operations;
	
	private static final String[] DEFAULT_LOCATIONS = new String[] {
			"META-INF/impala-bootstrap.xml",
			"META-INF/impala-osgi-bootstrap.xml"
	};

	public void start(BundleContext bundleContext) throws Exception {

		String[] locations = getBootstrapLocations(bundleContext);
	
		ImpalaOsgiApplicationContext applicationContext = startContext(bundleContext, locations);
		
		if (applicationContext != null) {
			initApplicationContext(bundleContext, applicationContext);
		}
			
	}

    void initApplicationContext(BundleContext bundleContext, ApplicationContext applicationContext) {
	   
	   	//TODO introduce constants for bean names
		ModuleManagementFacade facade = ObjectUtils.cast(applicationContext.getBean("moduleManagementFacade"),
				ModuleManagementFacade.class);
		
		if (facade == null) {
			throw new InvalidStateException("Application context '" + applicationContext.getDisplayName() 
					+ "' does not contain bean named 'moduleManagementFacade'");
		}
		
		setNewOperationsFacade(facade);
		bundleContext.registerService(OperationsFacade.class.getName(), operations, null);
		
		ModuleDefinitionSource moduleDefinitionSource = maybeGetModuleDefinitionSource(bundleContext, facade);
		
		if (moduleDefinitionSource != null) {
			operations.init(moduleDefinitionSource);
		}
		
	}

	void setNewOperationsFacade(ModuleManagementFacade facade) {
		operations = newOperationsFacade(facade);
	}

	InternalOperationsFacade newOperationsFacade(ModuleManagementFacade facade) {
		return new SimpleOperationsFacade(facade);
	}

	ModuleDefinitionSource maybeGetModuleDefinitionSource(BundleContext bundleContext, ModuleManagementFacade facade) {
		
		ModuleDefinitionSource moduleDefinitionSource = null;
		
		//TODO use dictionary property
		ServiceReference serviceReference = bundleContext.getServiceReference(ModuleDefinitionSource.class.getName());
		
		if (serviceReference != null) {
			Object service = bundleContext.getService(serviceReference);
			moduleDefinitionSource = ObjectUtils.cast(service, ModuleDefinitionSource.class);
		}
		
		if (moduleDefinitionSource == null) {
		
			//FIXME should do nothing here, rather than load a default set of modules.
			//Alternatively, could look for a set of bundles from the extender's property file.
			
			//TODO this needs to be picked up by a fragment
			/*
			moduleDefinitionSource = new InternalModuleDefinitionSource(
					facade.getTypeReaderRegistry(), 
					facade.getModuleLocationResolver(), 
					new String[]{ "osgi-root", "osgi-module1" });
			*/
		}
		return moduleDefinitionSource;
	}

	ImpalaOsgiApplicationContext startContext(BundleContext bundleContext, String[] locations) {
		
		OsgiContextStarter contextStarter = newContextStarter();
		contextStarter.setBundleContext(bundleContext);
		
		ApplicationContext context = contextStarter.startContext(Arrays.asList(locations));
		ImpalaOsgiApplicationContext applicationContext = ObjectUtils.cast(context, ImpalaOsgiApplicationContext.class);
		return applicationContext;
	}

	OsgiContextStarter newContextStarter() {
		
		OsgiContextStarter contextStarter = new OsgiContextStarter();
		return contextStarter;
	}

	String[] getBootstrapLocations(BundleContext bundleContext) {
		
		URL bootstrapLocationsResource = getBootstrapLocationsResourceURL(bundleContext);
		
		String[] locations = null;
		
		if (bootstrapLocationsResource != null) {
			final Properties resourceProperties = PropertyUtils.loadProperties(bootstrapLocationsResource);
			String locationString = resourceProperties.getProperty("bootstrapLocations");
			
			if (locationString != null) {
				locations = locationString.split(",");
			}		
		}
		
		if (locations == null) {
			locations = DEFAULT_LOCATIONS;
		}
		return locations;
	}

	URL getBootstrapLocationsResourceURL(BundleContext bundleContext) {
		
		String bootstrapLocationsResourceName = "impala.properties";
		URL bootstrapLocationsResource = OsgiUtils.findResource(bundleContext, bootstrapLocationsResourceName);
		return bootstrapLocationsResource;
	}

	/**
	 * Unloads Impala modules and shuts down Impala's {@link ApplicationContext}.
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		
		if (operations != null) {		
			operations.unloadRootModule();
			operations.getModuleManagementFacade().close();
		}
	}
}

