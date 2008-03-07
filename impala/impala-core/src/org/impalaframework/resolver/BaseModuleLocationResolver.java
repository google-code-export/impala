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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.impalaframework.exception.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public abstract class BaseModuleLocationResolver extends AbstractModuleLocationResolver {

	final Log logger = LogFactory.getLog(BaseModuleLocationResolver.class);

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
				logger.info("Resolved location property '" + propertyName + "' from system property: " + systemProperty);
			}
			value = systemProperty;
		}
		else {
			String suppliedValue = this.properties.getProperty(propertyName);
			if (suppliedValue != null) {
				if (logger.isInfoEnabled())
					logger.info("Resolved location property '" + propertyName + "' from supplied properties: " + suppliedValue);
				value = suppliedValue;
			}
			else {

				if (logger.isInfoEnabled())
					logger.info("Unable to resolve location '" + 
							propertyName + 
							"' from system property or supplied properties. Using default value: " + 
							defaultValue);
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

	protected String getWorkspaceRoot() {
		return properties.getProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
	}

	protected String getProperty(String key) {
		return properties.getProperty(key);
	}

}
