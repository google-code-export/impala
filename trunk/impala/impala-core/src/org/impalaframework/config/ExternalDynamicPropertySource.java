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

package org.impalaframework.config;

import org.impalaframework.util.PathUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ExternalDynamicPropertySource extends
		DynamicPropertiesPropertySource {

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
		String location = PathUtils.getPath(getAlternativeFolderLocation(), fileName);
		FileSystemResource fileResource = new FileSystemResource(location);
		ClassPathResource classPathResource = new ClassPathResource(fileName);

		Resource[] locations = new Resource[]{ classPathResource, fileResource };
		
		if (fileResource.exists()) {
			locations =  new Resource[]{ classPathResource, fileResource };
		} else {
			locations = new Resource[] { classPathResource };
		}
		return locations;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
