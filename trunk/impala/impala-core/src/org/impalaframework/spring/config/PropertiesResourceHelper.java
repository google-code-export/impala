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

/**
 * Class with helper methods related to 
 * 
 * @author Phil Zoio
 */
public class PropertiesResourceHelper  {

    private static final Log log = LogFactory.getLog(PropertiesResourceHelper.class);
    
    public static final String DEFAULT_PROPERTY_FOLDER_SYSTEM_PROPERTY = "property.folder";

    public static String getAlternativeFolderLocation(String propertyFolderSystemProperty) {
        if (propertyFolderSystemProperty == null) {
            propertyFolderSystemProperty = DEFAULT_PROPERTY_FOLDER_SYSTEM_PROPERTY;
        }

        String folderLocation = System.getProperty(propertyFolderSystemProperty);
        return folderLocation;
    }

    public static Resource[] getClassPathAndFileSystemLocations(String fileName, String alternativeFolderLocation) {

        final ClassPathResource classPathResource = new ClassPathResource(fileName);
        
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

}
