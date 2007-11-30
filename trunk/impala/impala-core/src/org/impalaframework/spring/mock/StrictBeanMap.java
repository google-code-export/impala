package org.impalaframework.spring.mock;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class StrictBeanMap extends BeanMap {

	public Object getBean(String name) throws BeansException {
		Object object = super.getBean(name);
		if (object == null) {
			throw new NoSuchBeanDefinitionException(name);
		}
		return object;
	}

}
