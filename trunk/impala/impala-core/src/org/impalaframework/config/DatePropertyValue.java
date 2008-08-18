package org.impalaframework.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class DatePropertyValue extends BasePropertyValue implements InitializingBean {
	
	private static final Log logger = LogFactory.getLog(DatePropertyValue.class);	

	private Date defaultValue;
	private String rawValue;
	private String pattern;
	private Date value;
	
	public void init() {
		Assert.notNull(pattern, "Pattern cannot be null");
	}

	public void afterPropertiesSet() throws Exception {
		init();
	}

	public synchronized Date getValue() {
		String rawValue = super.getRawValue();
		if (rawValue == null) {
			value = defaultValue;
		}
		else if (!rawValue.equals(this.rawValue)) {
			try {
				this.value = new SimpleDateFormat(pattern).parse(rawValue);
				this.rawValue = rawValue;
			} catch (ParseException e) {
				logger.error("Property " + rawValue + " is not a number");
			}
		}
		return value;
	}

	public void setDefaultValue(Date defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
}
