package org.impalaframework.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IntPropertyValue extends BasePropertyValue {
	
	private static final Log logger = LogFactory.getLog(DynamicPropertiesPropertySource.class);	

	private int defaultValue;
	private String rawValue;
	private int value;

	public synchronized int getValue() {
		String rawValue = super.getRawValue();
		if (rawValue == null) {
			value = defaultValue;
		}
		else if (!rawValue.equals(this.rawValue)) {
			try {
				this.value = Integer.parseInt(rawValue);
				this.rawValue = rawValue;
			} catch (NumberFormatException e) {
				logger.error("Property " + rawValue + " is not a number");
			}
		}
		return value;
	}

	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}
