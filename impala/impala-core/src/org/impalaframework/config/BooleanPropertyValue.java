package org.impalaframework.config;


public class BooleanPropertyValue extends BasePropertyValue {

	private boolean defaultValue;
	private String rawValue;
	private boolean value;

	public synchronized boolean getValue() {
		String rawValue = super.getRawValue();
		if (rawValue == null) {
			value = defaultValue;
		} else if (!rawValue.equals(this.rawValue)) {
			this.value = Boolean.parseBoolean(rawValue);
			this.rawValue = rawValue;
		}
		return value;
	}

	public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

}
