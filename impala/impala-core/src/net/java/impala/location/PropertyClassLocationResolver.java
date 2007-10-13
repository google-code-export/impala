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

package net.java.impala.location;

import java.io.File;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class PropertyClassLocationResolver implements ClassLocationResolver {

	protected static final String FOLDER_SEPARATOR = "/";

	protected static final String PLUGIN_CLASS_DIR_PROPERTY = "impala.plugin.class.dir";

	protected static final String PLUGIN_SPRING_DIR_PROPERTY = "impala.plugin.spring.dir";

	protected static final String SYSTEM_PLUGIN_DIR = "impala.system.plugin.dir";

	protected static final String PARENT_TEST_DIR = "impala.plugin.test.dir";

	private static final Log log = LogFactory.getLog(PropertyClassLocationResolver.class);

	private Properties properties;

	public PropertyClassLocationResolver() {
		super();
		this.properties = new Properties();
		init();
	}

	public PropertyClassLocationResolver(Properties properties) {
		super();
		Assert.notNull(properties);
		this.properties = (Properties) properties.clone();
		init();
	}

	public File[] getPluginTestClassLocations(String parentName) {
		String suffix = StringUtils.cleanPath(getProperty(PARENT_TEST_DIR));
		String path = getPath(getRootDirectoryPath(), parentName);
		path = getPath(path, suffix);
		return new File[] { new File(path) };
	}

	public File[] getApplicationPluginClassLocations(String plugin) {
		String classDir = getProperty(PLUGIN_CLASS_DIR_PROPERTY);

		String path = getPath(getRootDirectoryPath(), plugin);
		path = getPath(path, classDir);
		return new File[] { new File(path) };
	}

	public File getSystemPluginClassLocation(String plugin) {
		String path = getSystemPluginClassLocationPath(plugin);
		return new File(path);
	}

	public File getSystemPluginSpringLocation(String plugin) {
		String path = getSystemPluginClassLocationPath(plugin);
		path = getPath(path, plugin + "-context.xml");
		return new File(path);
	}

	private String getSystemPluginClassLocationPath(String plugin) {
		String sysPluginDir = getProperty(SYSTEM_PLUGIN_DIR);

		if (sysPluginDir == null) {
			throw new IllegalStateException(
					"Property 'impala.system.plugin.dir' not set. You need this to use system plugins");
		}

		String path = getPath(getRootDirectoryPath(), sysPluginDir);
		// note that the resources for system plugins are found in the same
		// directory as the Spring resources
		String springDir = getProperty(PLUGIN_SPRING_DIR_PROPERTY);
		path = getPath(path, springDir);

		path = getPath(path, plugin);
		return path;
	}

	public File getApplicationPluginSpringLocation(String plugin) {
		//FIXME should the Spring resources should also be
		//found on the class path, rather than relative to the plugin root directory
		String springDir = getProperty(PLUGIN_SPRING_DIR_PROPERTY);

		String path = getPath(getRootDirectoryPath(), plugin);
		path = getPath(path, springDir);
		return new File(path, plugin + "-context.xml");
	}

	private void init() {

		// the system plugin directory. Note the default is null
		mergeProperty(SYSTEM_PLUGIN_DIR, null, null);

		// the plugin directory which is expected to contain classes
		mergeProperty(PLUGIN_CLASS_DIR_PROPERTY, "bin", null);

		// the plugin directory which is expected to plugin Spring context files
		mergeProperty(PLUGIN_SPRING_DIR_PROPERTY, "spring", null);

		// the parent directory in which tests are expected to be found
		mergeProperty(PARENT_TEST_DIR, "bin", null);
	}

	protected String getPath(String root, String suffix) {
		if (!suffix.startsWith(FOLDER_SEPARATOR)) {
			suffix = FOLDER_SEPARATOR + suffix;
		}
		String path = root + suffix;
		return path;
	}

	void mergeProperty(String propertyName, String defaultValue, String extraToSupplied) {
		String systemProperty = System.getProperty(propertyName);
		String value = null;

		if (systemProperty != null) {

			if (log.isInfoEnabled()) {
				log.info("Resolved location property '" + propertyName + "' from system property: " + systemProperty);
			}
			value = systemProperty;
		}
		else {
			String suppliedValue = this.properties.getProperty(propertyName);
			if (suppliedValue != null) {
				if (log.isInfoEnabled())
					log.info("Resolved location property '" + propertyName + "' from supplied properties: "
							+ suppliedValue);
				value = suppliedValue;
			}
			else {

				if (log.isInfoEnabled())
					log.info("Unable to resolve location '" + propertyName
							+ "' from system property or supplied properties. Using default value: " + defaultValue);
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
				throw new IllegalStateException("'workspace.root' (" + workspace + ") does not exist");
			}
			if (!candidate.isDirectory()) {
				throw new IllegalStateException("'workspace.root' (" + workspace + ") is not a directory");
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
