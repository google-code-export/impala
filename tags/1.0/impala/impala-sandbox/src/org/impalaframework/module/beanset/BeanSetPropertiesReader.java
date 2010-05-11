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

package org.impalaframework.module.beanset;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

public class BeanSetPropertiesReader {

	private String DEFAULT_BEANSET_PROPERTIES_FILE = "beanset.properties";

	private String ALL_BEANSETS = "all_beans";

	private static final Log logger = LogFactory.getLog(BeanSetPropertiesReader.class);

	/**
	 * Reads beanSet definition specified in the following format: "null: bean1,
	 * bean2; mock: bean3" will output a set of Properties where the spring
	 * context files for the beansets bean1 and bean2 are loaded from the file
	 * beanset_null.properties and the context files for authorisation are
	 * loaded from beanset_mock.properties. Uses beanset.properties as the
	 * default module definition
	 */
	public Properties readBeanSetDefinition(ClassLoader classLoader, String definitionString) {
		Assert.notNull(classLoader);
		Assert.notNull(definitionString);

		BeanSetMapReader reader = new BeanSetMapReader();
		final Map<String, Set<String>> definition = reader.readBeanSetDefinition(definitionString);

		return readBeanSetDefinition(classLoader, definition);
	}

	public Properties readBeanSetDefinition(ClassLoader classLoader, final Map<String, Set<String>> definitionMap) {

		Properties defaultProps = readProperties(classLoader, DEFAULT_BEANSET_PROPERTIES_FILE);

		final Set<String> keySet = definitionMap.keySet();
		for (String fileName : keySet) {

			String propertyFileFullName = propertyFileFullName(fileName);
			Properties overrides = readProperties(classLoader, propertyFileFullName);

			final Set<String> set = definitionMap.get(fileName);

			if (set.size() == 1 && ALL_BEANSETS.equals(set.iterator().next())) {
				readAllBeanSets(defaultProps, overrides, propertyFileFullName);
			}
			else {
				readSelectedBeanSets(defaultProps, overrides, propertyFileFullName, set);
			}

		}
		return defaultProps;
	}

	private void readAllBeanSets(Properties defaultProps, Properties overrides, String propertyFileFullName) {

		Set<Object> propertyList = overrides.keySet();

		for (Object moduleKey : propertyList) {
			String moduleName = moduleKey.toString().trim();
			String moduleFile = overrides.getProperty(moduleName);

			applyBeanSetFile(defaultProps, moduleName, moduleFile, propertyFileFullName);

		}
	}

	private void readSelectedBeanSets(Properties defaultProps, Properties overrides, String propertyFileFullName,
			Set<String> set) {
		for (String moduleName : set) {
			String moduleFile = overrides.getProperty(moduleName);
			applyBeanSetFile(defaultProps, moduleName, moduleFile, propertyFileFullName);
		}
	}

	private void applyBeanSetFile(Properties defaultProps, String moduleName, String moduleFile,
			String propertyFileFullName) {
		if (moduleFile == null) {
			logger.warn("Unable to find application context file name for module '" + propertyFileFullName
					+ "' in module properties file {}");
		}
		else {
			if (logger.isDebugEnabled()) {
				String existingValue = defaultProps.getProperty(moduleName);
				logger.debug("Overridding module file for module " + moduleName + " with " + moduleFile
						+ ", loaded from " + propertyFileFullName + ". Previous value: " + existingValue);
			}
			defaultProps.setProperty(moduleName, moduleFile);
		}
	}

	private String propertyFileFullName(String propertyFileName) {
		return "beanset_" + propertyFileName + ".properties";
	}

	protected Properties readProperties(ClassLoader classLoader, String fileName) {
		Properties properties = new Properties();
		try {
			properties.load(classLoader.getResourceAsStream(fileName));
		}
		catch (Exception e) {
			throw new FatalBeanException("Unable to load module definition file " + fileName + " on classpath.");
		}
		return properties;
	}

}
