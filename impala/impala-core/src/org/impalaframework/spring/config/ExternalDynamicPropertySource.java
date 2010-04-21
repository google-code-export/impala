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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Extension of {@link DynamicPropertySource} which additionally supports
 * loading properties from a folder on the file system. By default, this folder
 * can be specified using the system property <code>property.folder</code>.
 * The property {@link #propertyFolderSystemProperty} can be used to change this
 * default. If the system property is not specified or the folder is not present
 * on the file system, the superclass behaviour is observed.
 * 
 * @author Phil Zoio
 */
public class ExternalDynamicPropertySource extends
        DynamicPropertySource {
    
    private static final Log logger = LogFactory.getLog(ExternalDynamicPropertySource.class);
    
    private String fileName;
    private String propertyFolderSystemProperty;

    protected String getAlternativeFolderLocation() {
        return PropertiesResourceHelper.getAlternativeFolderLocation(propertyFolderSystemProperty);
    }
    
    public void setPropertyFolderSystemProperty(String systemPropertyName) {
        this.propertyFolderSystemProperty = systemPropertyName;
    }   
    
    @Override
    public void afterPropertiesSet() throws Exception {
        
        Assert.notNull(fileName, "fileName cannot be null");
        Resource[] locations = getLocations();

        DynamicPropertiesFactoryBean factoryBean = new DynamicPropertiesFactoryBean();
        factoryBean.setLocations(locations);
        super.setFactoryBean(factoryBean);
        super.afterPropertiesSet();
    }

    protected Resource[] getLocations() {
        final String alternativeFolderLocation = getAlternativeFolderLocation();
        final Resource[] locations = PropertiesResourceHelper.getClassPathAndFileSystemLocations(fileName, alternativeFolderLocation);
        
        logger.info("Loading properties from locations: " + locations);
        return locations;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
