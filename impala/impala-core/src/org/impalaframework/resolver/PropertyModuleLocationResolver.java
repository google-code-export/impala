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
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Phil Zoio
 */
public class PropertyModuleLocationResolver implements ModuleLocationResolver {

	final Logger logger = LoggerFactory.getLogger(PropertyModuleLocationResolver.class);

	private Properties properties;

	public PropertyModuleLocationResolver() {
		super();
		this.properties = new Properties();
		init();
	}

	public PropertyModuleLocationResolver(Properties properties) {
		super();
		Assert.notNull(properties);
		this.properties = (Properties) properties.clone();
		init();
	}

	public String getParentProject() {
		final String property = getProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
		if (property == null) {
			throw new ConfigurationException(
					"Unknown parent project. Can be specified using system property or in relevant execution properties file");
		}
		return property;
	}

	public List<Resource> getModuleTestClassLocations(String parentName) {
		String suffix = StringUtils.cleanPath(getProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY));
		String path = PathUtils.getPath(getRootDirectoryPath(), parentName);
		path = PathUtils.getPath(path, suffix);
		Resource fileSystemResource = new FileSystemResource(path);
		List<Resource> list = Collections.singletonList(fileSystemResource);
		return list;
	}

	public List<Resource> getApplicationModuleClassLocations(String plugin) {
		String classDir = getProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY);

		String path = PathUtils.getPath(getRootDirectoryPath(), plugin);
		path = PathUtils.getPath(path, classDir);
		Resource resource = new FileSystemResource(path);
		return Collections.singletonList(resource);
	}

	public File getSystemPluginClassLocation(String plugin) {
		String path = getSystemModuleClassLocationPath(plugin);
		return new File(path);
	}

	public File getSystemPluginSpringLocation(String plugin) {
		String path = getSystemModuleClassLocationPath(plugin);
		path = PathUtils.getPath(path, plugin + "-context.xml");
		return new File(path);
	}

	private String getSystemModuleClassLocationPath(String plugin) {
		String systemModuleDir = getProperty(LocationConstants.SYSTEM_MODULE_DIR_PROPERTY);

		if (systemModuleDir == null) {
			throw new ConfigurationException(
					"Property 'impala.system.module.dir' not set. You need this to use system plugins");
		}

		String path = PathUtils.getPath(getRootDirectoryPath(), systemModuleDir);
		// note that the resources for system plugins are found in the same
		// directory as the Spring resources
		String springDir = getProperty(LocationConstants.MODULE_SPRING_DIR_PROPERTY);
		path = PathUtils.getPath(path, springDir);

		path = PathUtils.getPath(path, plugin);
		return path;
	}

	public Resource getApplicationModuleSpringLocation(String plugin) {
		String springDir = getProperty(LocationConstants.MODULE_SPRING_DIR_PROPERTY);

		String path = PathUtils.getPath(getRootDirectoryPath(), plugin);
		path = PathUtils.getPath(path, springDir);
		return new FileSystemResource(new File(path, plugin + "-context.xml").getAbsolutePath());
	}

	private void init() {

		// the parent directory in which tests are expected to be found
		mergeProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, null, null);

		// the system plugin directory. Note the default is null
		mergeProperty(LocationConstants.SYSTEM_MODULE_DIR_PROPERTY, null, null);

		// the plugin directory which is expected to contain classes
		mergeProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY, "bin", null);

		// the plugin directory which is expected to plugin Spring context files
		mergeProperty(LocationConstants.MODULE_SPRING_DIR_PROPERTY, "spring", null);

		// the parent directory in which tests are expected to be found
		mergeProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY, "bin", null);
	}

	void mergeProperty(String propertyName, String defaultValue, String extraToSupplied) {
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
					logger.info("Unable to resolve location '{}' from system property or supplied properties. Using default value: {}",
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

	protected File getRootDirectory() {
		String workspace = properties.getProperty("workspace.root");
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
		return new File("../");
	}

	protected String getRootDirectoryPath() {
		File rootDirectory = getRootDirectory();
		String absolutePath = rootDirectory.getAbsolutePath();
		String cleanPath = StringUtils.cleanPath(absolutePath);
		return cleanPath;
	}

	protected String getProperty(String key) {
		return properties.getProperty(key);
	}

}
