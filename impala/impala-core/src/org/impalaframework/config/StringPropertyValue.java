package org.impalaframework.config;

import org.springframework.beans.factory.FactoryBean;

public class StringPropertyValue extends BasePropertyValue implements FactoryBean {

	public Object getObject() throws Exception {
		return getValue();
	}

	public Class<?> getObjectType() {
		return String.class;
	}

	public boolean isSingleton() {
		return false;
	}

}
