package org.impalaframework.config;

import org.impalaframework.util.PathUtils;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
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

		PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
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
