/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * {@link BeanFactory} which returns a particular named Spring bean. Use the {@link #beanName} property to specify which 
 * Spring bean should actually be returned.
 */
public class NamedFactoryBean implements FactoryBean, BeanFactoryAware, InitializingBean {

	private BeanFactory beanFactory;

	private String beanName;

	private Class<?> objectType;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(objectType);
		Assert.notNull(beanName);
	}

	public Object getObject() throws Exception {
		// beanFactory won't permit invalid type to be returned
		return beanFactory.getBean(beanName, objectType);
	}

	public Class<?> getObjectType() {
		return objectType;
	}

	public boolean isSingleton() {
		return true;
	}
	
	/* ************* BeanFactoryAware implementation ************ */

	/**
	 * Sets the <code>BeanFactory</code> from which the bean is returned.
	 * Implementation method of the <code>BeanFactoryAware</code> interface.
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/* ************* injected types ************ */

	/**
	 * Injection property, setting the name of the bean to be returned using <code>getObject()</code>. Required.
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	/**
	 * Injection property, setting the type of the bean to be returned using <code>getObjectType()</code>. Required.
	 */
	public void setObjectType(Class<?> objectType) {
		this.objectType = objectType;
	}

}
