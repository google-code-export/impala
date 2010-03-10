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

package org.impalaframework.config;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.constants.LocationConstants;
import org.impalaframework.util.PropertyUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

public class SimplePropertiesLoader implements PropertiesLoader {

    private static final Log logger = LogFactory.getLog(SimplePropertiesLoader.class);
    
    private String defaultBootstrapResource;
    
    public SimplePropertiesLoader(String defaultBootstrapResource) {
        super();
        Assert.notNull(defaultBootstrapResource, "defaultBootstrapResource cannot be null");
        this.defaultBootstrapResource = defaultBootstrapResource;
    }

    public Properties loadProperties() {
        return getProperties();
    }
    
    protected Properties getProperties() {
        
        String bootstrapLocationsResource = getResourceName();

        ResourceLoader resourceLoader = getResourceLoader();
        Resource bootStrapResource = null;
        
        if (bootstrapLocationsResource == null) {
            bootStrapResource = resourceLoader.getResource(defaultBootstrapResource);
        }
        else {
            // figure out which resource loader to use
            bootStrapResource = resourceLoader.getResource(bootstrapLocationsResource);
        }
        Properties properties = null;
        if (bootStrapResource == null || !bootStrapResource.exists()) {
            logger.info("Unable to load locations resource from " + bootstrapLocationsResource + ".");
            properties = new Properties();
        } else { 
            properties = PropertyUtils.loadProperties(bootStrapResource);
        }
        
        return properties;
    }

    protected String getResourceName() {
        String bootstrapLocationsResource = System.getProperty(LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
        return bootstrapLocationsResource;
    }
    
    protected ResourceLoader getResourceLoader() {
        return new DefaultResourceLoader();
    }

    public void setDefaultBootstrapResource(String defaultResource) {
        Assert.notNull(defaultResource);
        this.defaultBootstrapResource = defaultResource;
    }
}
