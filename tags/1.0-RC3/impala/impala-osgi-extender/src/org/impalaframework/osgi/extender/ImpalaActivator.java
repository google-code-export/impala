/*
 * Copyright 2007-2010 the original author or authors.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.config.PrefixedCompositePropertySource;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.PropertySourceHolder;
import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.config.SystemPropertiesPropertySource;
import org.impalaframework.exception.ExecutionException;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.facade.Impala;
import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.facade.SimpleOperationsFacade;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.source.InternalXmlModuleDefinitionSource;
import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContext;
import org.impalaframework.osgi.startup.OsgiContextStarter;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.PropertyUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.UrlResource;

/**
 * Impala OSGi activator class. Responsible for two things: bootstrapping Impala itself, then loading the modules managed by
 * Impala. The Spring configuration files used to bootstrap Impala will by default be found using the property <i>bootstrapLocations</i>
 * in the file <i>impala.properties</i>, which will typically be made accessible to the activator using a fragment whose host 
 * is the extender bundle (that is, the bundle which contains this class). If this resource is not present, then 
 * the default locations "META-INF/impala-bootstrap.xml" and "META-INF/impala-osgi-bootstrap.xml" are used.
 * 
 * The module definitions to be loaded can be done via one of two means. One, by using a <code>ModuleDefinitionSource</code>
 * service, for example {@link org.impalaframework.osgi.test.InjectableModuleDefinitionSource} registered as an OSGi service.
 * Two, by adding <i>moduledefinitions.xml</i> into the extender bundle, again using an extender fragment, as in the case
 * with the Impala bootstrap locations.
 * @author Phil Zoio
 */
public class ImpalaActivator implements BundleActivator {

    private static Log logger = LogFactory.getLog(ImpalaActivator.class);
    
    private InternalOperationsFacade operations;
    
    private static final String[] DEFAULT_LOCATIONS = new String[] {
            "META-INF/impala-bootstrap.xml",
            "META-INF/impala-graph-bootstrap.xml",
            "META-INF/impala-osgi-bootstrap.xml"
    };

    public void start(BundleContext bundleContext) throws Exception {

        String[] locations = getBootstrapLocations(bundleContext);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Called start for bundle context from bundle '" + bundleContext.getBundle() + "'. Activator class: " + this.getClass().getName());
        }
    
        ImpalaOsgiApplicationContext applicationContext = startContext(bundleContext, locations);
        
        if (applicationContext != null) {
            
            logger.info("Started Impala application context for bundle '" + bundleContext.getBundle() + "': " + applicationContext);
            initApplicationContext(bundleContext, applicationContext);
            
        } else {
            logger.warn("No Impala application context started for bundle '" + bundleContext.getBundle() + "'");
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
            
            logger.info("Found module definition source for bootstrapping Impala modules: " + moduleDefinitionSource);
            operations.init(moduleDefinitionSource);
            
        } else {
            
            logger.info("No module definition source found for bootstrapping Impala modules. No modules loaded");
        }
        
    }

    void setNewOperationsFacade(ModuleManagementFacade facade) {
        operations = newOperationsFacade(facade);
        Impala.init(operations);
    }

    InternalOperationsFacade newOperationsFacade(ModuleManagementFacade facade) {
        return new SimpleOperationsFacade(facade);
    }

    ModuleDefinitionSource maybeGetModuleDefinitionSource(BundleContext bundleContext, ModuleManagementFacade facade) {
        
        ModuleDefinitionSource moduleDefinitionSource = null;
        
        //TODO use dictionary property to distinguish between possible different services with the same interface
        ServiceReference serviceReference = bundleContext.getServiceReference(ModuleDefinitionSource.class.getName());
        
        if (serviceReference != null) {
            Object service = bundleContext.getService(serviceReference);
            moduleDefinitionSource = ObjectUtils.cast(service, ModuleDefinitionSource.class);
        
            if (logger.isDebugEnabled()) {
                logger.debug("Found module definitionSource injected into OSGi service registry: " + moduleDefinitionSource);
            }
        } else {
            
            if (logger.isDebugEnabled()) {
                logger.debug("No module definitionSource injected into OSGi service registry.");
            }
        }
        
        if (moduleDefinitionSource == null) {
            //TODO test this with a real example
            moduleDefinitionSource = maybeLoadXmlResource(bundleContext, facade);
        }
        return moduleDefinitionSource;
    }

    ModuleDefinitionSource maybeLoadXmlResource(BundleContext bundleContext, ModuleManagementFacade facade) {
        
        URL moduleDefinitionResourceURL = getModuleDefinitionsResourceURL(bundleContext);
        if (moduleDefinitionResourceURL != null) {
            
            if (logger.isDebugEnabled()) {
                logger.debug("Found module definition reource URL from bundle context: " + moduleDefinitionResourceURL);
            }
            
            InternalXmlModuleDefinitionSource source = new InternalXmlModuleDefinitionSource(facade.getModuleLocationResolver(), facade.getTypeReaderRegistry());
            source.setResource(new UrlResource(moduleDefinitionResourceURL));
            return source;
        } else {
            
            if (logger.isDebugEnabled()) {
                logger.debug("No module definition reource URL from bundle context");
            }
        }
        
        return null;
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
            
            try {
                final Properties resourceProperties = PropertyUtils.loadProperties(bootstrapLocationsResource);
                
                //FIXME use PropertiesLoader implementation
                
                List<PropertySource> propertySources = new ArrayList<PropertySource>();
                
                //property value sought first in system property
                propertySources.add(new SystemPropertiesPropertySource());
                
                //then in impala properties file
                propertySources.add(new StaticPropertiesPropertySource(resourceProperties));

                PrefixedCompositePropertySource propertySource = new PrefixedCompositePropertySource("impala.", propertySources);
                
                PropertySourceHolder.getInstance().setPropertySource(propertySource);               
                
                String locationString = resourceProperties.getProperty("bootstrapLocations");
                
                if (locationString != null) {
                    locations = locationString.split(",");
                }       
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Using Impala bootstrap locations found from resource '" + bootstrapLocationsResource + ": " + Arrays.toString(locations));
                }
            } catch (ExecutionException e) {
                logger.error("Unable to load Impala bootstrap resource from specified location: " + e.getMessage(), e);
                throw e;
            }
        }
        
        if (locations == null) {
            locations = DEFAULT_LOCATIONS;
            
            if (logger.isDebugEnabled()) {
                logger.debug("Using default Impala bootstrap locations: " + Arrays.toString(locations));
            }
        }
        
        return locations;
    }

    protected URL getBootstrapLocationsResourceURL(BundleContext bundleContext) {
        //TODO allow this location to be overridden using System property
        return OsgiUtils.findResource(bundleContext, "impala.properties");
    }
    
    protected URL getModuleDefinitionsResourceURL(BundleContext bundleContext) {
        //TODO allow this location to be overridden using System property
        return OsgiUtils.findResource(bundleContext, "moduledefinitions.xml");
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

