package org.impalaframework.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FloatPropertyValue extends BasePropertyValue {
	
	private static final Log logger = LogFactory.getLog(FloatPropertyValue.class);	

	private float defaultValue;
	private String rawValue;
	private float value;

	public synchronized float getValue() {
		String rawValue = super.getRawValue();
		if (rawValue == null) {
			value = defaultValue;
		}
		else if (!rawValue.equals(this.rawValue)) {
			try {
				this.value = Float.parseFloat(rawValue);
				this.rawValue = rawValue;
			} catch (NumberFormatException e) {
				logger.error("Property " + rawValue + " is not a number");
			}
		}
		return value;
	}

	public void setDefaultValue(float defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}
