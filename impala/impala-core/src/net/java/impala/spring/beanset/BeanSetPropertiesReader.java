package net.java.impala.spring.beanset;

import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

public class BeanSetPropertiesReader {

	private String DEFAULT_MODULE_PROPERTIES_FILE = "beanset.properties";

	private String ALL_MODULES = "all_beans";

	private static final Log log = LogFactory.getLog(BeanSetPropertiesReader.class);

	/**
	 * Reads module specification specified in the following format: "null:
	 * bean1, bean2; mock: bean3" will output a set of Properties where the spring
	 * context files for the beansets bean1 and bean2 are loaded from
	 * the file beanset_null.properties and the context files for authorisation
	 * are loaded from beanset_mock.properties. Uses beanset.properties as the
	 * default module specification
	 */
	public Properties readModuleSpec(String definition) {
		Assert.notNull(definition);

		Properties defaultProps = readProperties(DEFAULT_MODULE_PROPERTIES_FILE);

		String[] beanSetLists = definition.split(";");

		for (String beanSetList : beanSetLists) {
			int colonIndex = beanSetList.indexOf(':');

			if (colonIndex < 0) {
				throw new FatalBeanException("Invalid beanset specification. Missing ':' from string '" + beanSetList
						+ "' in '" + definition + "'");
			}

			String fileName = beanSetList.substring(0, colonIndex).trim();

			String propertyFileFullName = propertyFileFullName(fileName);
			Properties overrides = readProperties(propertyFileFullName);

			String propertyListString = beanSetList.substring(colonIndex + 1).trim();

			if (propertyListString.length() > 0) {

				propertyListString = propertyListString.trim();

				if (propertyListString.equals(ALL_MODULES)) {
					// we simply add all of the module definitions into the
					// property set
					readAllModules(defaultProps, overrides, propertyFileFullName);

				}
				else {
					// add the named modules into the property set
					readSelectedModules(defaultProps, overrides, propertyFileFullName, propertyListString);
				}

			}

		}

		return defaultProps;
	}

	private void readAllModules(Properties defaultProps, Properties overrides, String propertyFileFullName) {

		Set<Object> propertyList = overrides.keySet();

		for (Object moduleKey : propertyList) {
			String moduleName = moduleKey.toString().trim();
			String moduleFile = overrides.getProperty(moduleName);

			applyModuleFile(defaultProps, moduleName, moduleFile, propertyFileFullName);

		}
	}

	private void readSelectedModules(Properties defaultProps, Properties overrides, String propertyFileFullName,
			String propertyListString) {
		String[] propertyList = propertyListString.split(",");

		for (String moduleName : propertyList) {
			moduleName = moduleName.trim();
			String moduleFile = overrides.getProperty(moduleName);

			applyModuleFile(defaultProps, moduleName, moduleFile, propertyFileFullName);

		}
	}

	private void applyModuleFile(Properties defaultProps, String moduleName, String moduleFile,
			String propertyFileFullName) {
		if (moduleFile == null) {
			log.warn("Unable to find application context file name for module '" + moduleName
					+ "' in module properties file " + propertyFileFullName);
		}
		else {
			if (log.isDebugEnabled()) {
				String existingValue = defaultProps.getProperty(moduleName);
				log.debug("Overridding module file for module " + moduleName + " with " + moduleFile + ", loaded from "
						+ propertyFileFullName + ". Previous value: " + existingValue);
			}
			defaultProps.setProperty(moduleName, moduleFile);
		}
	}

	private String propertyFileFullName(String propertyFileName) {
		return "beanset_" + propertyFileName + ".properties";
	}

	protected Properties readProperties(String fileName) {
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(fileName));
		}
		catch (Exception e) {
			throw new FatalBeanException("Unable to load module definition file " + fileName + " on classpath.");
		}
		return properties;
	}

}
