package org.impalaframework.spring.mock;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.Assert;

public class BeanMap {

	private Map<String, Object> beanMap = new HashMap<String, Object>();

	public Object getBean(String name) throws BeansException {
		Assert.notNull(name);
		Object object = beanMap.get(name);
		if (object == null) {
			throw new NoSuchBeanDefinitionException(name);
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	public Object getBean(String name, Class requiredType) throws BeansException {
		Object bean = getBean(name);
		Assert.isTrue(requiredType.isAssignableFrom(bean.getClass()), "bean must be an instance of "
				+ requiredType.getClass().getName());
		return bean;
	}

	public void putBean(String beanName, Object bean) {
		beanMap.put(beanName, bean);
	}

	public boolean containsBean(String name) {
		return beanMap.containsKey(name);
	}

	public Class getType(String name) throws NoSuchBeanDefinitionException {
		return getBean(name).getClass();
	}



}
