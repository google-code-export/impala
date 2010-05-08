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

package org.impalaframework.spring.config;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Extension of {@link PropertiesFactoryBean} which allows optional loading of properties from file system resource as well as 
 * class path, for given file name. Note that the file system value for the property concerned with override the classpath value.
 * 
 * @author Phil Zoio
 */
public class ExternalPropertiesFactoryBean extends
        PropertiesFactoryBean {
    
    private static final Log logger = LogFactory.getLog(ExternalDynamicPropertySource.class);
    
    private String fileName;
    private String propertyFolderSystemProperty;
    
    @Override
    protected Object createInstance() throws IOException {
        
        Assert.notNull(fileName, "fileName cannot be null");
        
        final Resource[] locations = getLocations();
        super.setLocations(locations);
        return super.createInstance();
    }

    Resource[] getLocations() {
        final String alternativeFolderLocation = PropertiesResourceHelper.getAlternativeFolderLocation(propertyFolderSystemProperty);
        final Resource[] locations = PropertiesResourceHelper.getClassPathAndFileSystemLocations(fileName, alternativeFolderLocation);        
        
        logger.info("Loading properties from locations: " + locations);
        return locations;
    }
    
    public void setPropertyFolderSystemProperty(String systemPropertyName) {
        this.propertyFolderSystemProperty = systemPropertyName;
    }   

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
