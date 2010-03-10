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
import org.impalaframework.util.PathUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
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

    private Log log = LogFactory.getLog(DynamicPropertiesFactoryBean.class);
    
    private String fileName;
    private String propertyFolderSystemProperty;

    public static final String DEFAULT_PROPERTY_FOLDER_SYSTEM_PROPERTY = "property.folder";

    protected String getAlternativeFolderLocation() {
        if (propertyFolderSystemProperty == null) {
            propertyFolderSystemProperty = DEFAULT_PROPERTY_FOLDER_SYSTEM_PROPERTY;
        }

        String folderLocation = System.getProperty(propertyFolderSystemProperty);
        return folderLocation;
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

        final ClassPathResource classPathResource = new ClassPathResource(fileName);
        
        final String alternativeFolderLocation = getAlternativeFolderLocation();
        if (alternativeFolderLocation == null) {
            return new Resource[]{ classPathResource };
        }
        
        String location = PathUtils.getPath(alternativeFolderLocation, fileName);
        FileSystemResource fileResource = new FileSystemResource(location);
        Resource[] locations = new Resource[]{ classPathResource, fileResource };
        
        if (fileResource.exists()) {
            locations =  new Resource[]{ classPathResource, fileResource };
        } else {
            log.warn("File system location for property resources '" + location + "' does not exist");
            locations = new Resource[] { classPathResource };
        }
        return locations;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
