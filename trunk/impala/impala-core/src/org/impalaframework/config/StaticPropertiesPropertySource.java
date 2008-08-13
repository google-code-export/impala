package org.impalaframework.config;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StaticPropertiesPropertySource implements PropertySource {

	private static final Log logger = LogFactory.getLog(StaticPropertiesPropertySource.class);
	
	private Properties properties;
	
	public String getValue(String name) {
		if (properties == null) {
			logger.warn("Properties is null for property keyed by name '" + name + "'");
			return null;
		}
		return properties.getProperty(name);
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
