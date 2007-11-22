package org.impalaframework.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class NamedFactoryBean implements FactoryBean, BeanFactoryAware, InitializingBean {

	private BeanFactory beanFactory;

	private String beanName;

	private Class objectType;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(objectType);
		Assert.notNull(beanName);
	}
	
	public Object getObject() throws Exception {
		//beanFactory won't permit invalid type to be returned
		return beanFactory.getBean(beanName, objectType);
	}

	public Class getObjectType() {
		return objectType;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/* ************* injected types ************ */
	
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setObjectType(Class objectType) {
		this.objectType = objectType;
	}

}
