package org.impalaframework.config;

import org.springframework.util.Assert;

public class BasePropertyValue {
	
	private String name;

	private PropertySource propertiesSource;

	protected String getValue() {
		Assert.notNull(propertiesSource, "propertiesSource must be specified");
		Assert.notNull(name, "name must be specified");
		return propertiesSource.getValue(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPropertiesSource(PropertySource propertiesSource) {
		this.propertiesSource = propertiesSource;
	}

}
