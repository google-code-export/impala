package org.impalaframework.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DoublePropertyValue extends BasePropertyValue {
	
	private static final Log logger = LogFactory.getLog(DoublePropertyValue.class);	

	private double defaultValue;
	private String rawValue;
	private double value;

	public synchronized double getValue() {
		String rawValue = super.getRawValue();
		if (rawValue == null) {
			value = defaultValue;
		}
		else if (!rawValue.equals(this.rawValue)) {
			try {
				this.value = Double.parseDouble(rawValue);
				this.rawValue = rawValue;
			} catch (NumberFormatException e) {
				logger.error("Property " + rawValue + " is not a number");
			}
		}
		return value;
	}

	public void setDefaultValue(double defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}
