/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.resolver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.impalaframework.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Phil Zoio
 */
public abstract class BaseModuleLocationResolver implements ModuleLocationResolver {

	final Logger logger = LoggerFactory.getLogger(BaseModuleLocationResolver.class);

	private Properties properties;

	public BaseModuleLocationResolver() {
		super();
		this.properties = new Properties();
	}

	public BaseModuleLocationResolver(Properties properties) {
		super();
		Assert.notNull(properties);
		this.properties = (Properties) properties.clone();
	}

	protected void init() {
		// the parent directory in which tests are expected to be found
		mergeProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, null, null);
		mergeProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, null, null);
	}

	public List<String> getRootProjects() {
		final String property = getProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
		if (property == null) {
			throw new ConfigurationException(
					"Unknown root projects. Can be specified using system property or in relevant execution properties file");
		}

		List<String> allLocations = new ArrayList<String>();

		String[] rootProjects = property.split(",");
		for (String rootProjectName : rootProjects) {
			allLocations.add(rootProjectName.trim());
		}

		return allLocations;
	}

	protected void mergeProperty(String propertyName, String defaultValue, String extraToSupplied) {
		String systemProperty = System.getProperty(propertyName);
		String value = null;

		if (systemProperty != null) {

			if (logger.isInfoEnabled()) {
				logger.info("Resolved location property '{}' from system property: {}", propertyName, systemProperty);
			}
			value = systemProperty;
		}
		else {
			String suppliedValue = this.properties.getProperty(propertyName);
			if (suppliedValue != null) {
				if (logger.isInfoEnabled())
					logger.info("Resolved location property '{}' from supplied properties: {}", propertyName,
							suppliedValue);
				value = suppliedValue;
			}
			else {

				if (logger.isInfoEnabled())
					logger
							.info(
									"Unable to resolve location '{}' from system property or supplied properties. Using default value: {}",
									propertyName, defaultValue);
				value = defaultValue;
			}
		}
		if (value != null) {
			if (extraToSupplied != null) {
				if (!value.endsWith(extraToSupplied)) {
					value += extraToSupplied;
				}
			}

			this.properties.put(propertyName, value);
		}
	}

	public File getRootDirectory() {
		String workspace = properties.getProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
		if (workspace != null) {
			File candidate = new File(workspace);

			if (!candidate.exists()) {
				throw new ConfigurationException("'workspace.root' (" + workspace + ") does not exist");
			}
			if (!candidate.isDirectory()) {
				throw new ConfigurationException("'workspace.root' (" + workspace + ") is not a directory");
			}
			return candidate;
		}
		return null;
	}

	protected String getRootDirectoryPath() {
		File rootDirectory = getRootDirectory();
		String absolutePath = rootDirectory.getAbsolutePath();
		return StringUtils.cleanPath(absolutePath);
	}

	protected String getProperty(String key) {
		return properties.getProperty(key);
	}

}
