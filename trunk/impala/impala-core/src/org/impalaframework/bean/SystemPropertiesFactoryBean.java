package org.impalaframework.bean;

import java.util.Properties;

public class SystemPropertiesFactoryBean extends SystemPropertyFactoryBean {

	private Properties properties;

	@Override
	protected String getValueIfPropertyNotFound() {
		String value = null;
		if (properties != null) {
			value = properties.getProperty(getPropertyName());
		}
		if (value == null)
			value = super.getValueIfPropertyNotFound();
		return value;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
