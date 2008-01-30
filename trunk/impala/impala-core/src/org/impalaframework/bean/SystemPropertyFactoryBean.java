package org.impalaframework.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class SystemPropertyFactoryBean implements FactoryBean, InitializingBean {

	private String defaultValue;
	
	private String propertyName;
	
	private String value;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(propertyName, "propertyName must be set");
		String value = System.getProperty(propertyName);
		
		if (value == null) {
			value = getValueIfPropertyNotFound();
		}
		this.value = value;
	}

	protected String getValueIfPropertyNotFound() {
		return defaultValue;
	}
	
	public Object getObject() throws Exception {
		return value;
	}

	public Class<?> getObjectType() {
		return String.class;
	}

	public boolean isSingleton() {
		return true;
	}

	protected String getPropertyName() {
		return propertyName;
	}

	/* **************** injected setters ************** */
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

}
