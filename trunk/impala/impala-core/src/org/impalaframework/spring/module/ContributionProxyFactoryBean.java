/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.spring.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * <code>FactoryBean</code> which creates a proxy which has uses
 * <code>SimpleContributionEndpointTargetSource</code> as a target source and
 * <code>ContributionEndpointInterceptor</code> as interceptor
 * 
 * @author Phil Zoio
 */
public class ContributionProxyFactoryBean implements FactoryBean, BeanNameAware, InitializingBean, ContributionEndpoint {

	final Logger logger = LoggerFactory.getLogger(ContributionProxyFactoryBean.class);

	private static final long serialVersionUID = 1L;

	private Class<?>[] interfaces;

	private String beanName;

	private ProxyFactory proxyFactory;

	private ContributionEndpointTargetSource targetSource;
	
	private boolean allowNoService;

	/* *************** BeanNameAware implementation method ************** */

	public void setBeanName(String name) {
		this.beanName = name;
	}

	/* *************** InitializingBean implementation method ************** */

	public void afterPropertiesSet() throws Exception {

		setDefaults();

		this.proxyFactory = new ProxyFactory();
		for (int i = 0; i < interfaces.length; i++) {
			if (logger.isDebugEnabled()) {
				logger.debug("Adding interface " + interfaces[i] + " loaded from " + interfaces[i].getClassLoader());
			}
			proxyFactory.addInterface(interfaces[i]);
		}
		proxyFactory.setTargetSource(targetSource);
		ContributionEndpointInterceptor interceptor = new ContributionEndpointInterceptor(targetSource, beanName);
		interceptor.setProceedWithNoService(allowNoService);
		proxyFactory.addAdvice(interceptor);
	}

	void setDefaults() {
		// this is the default
		if (targetSource == null)
			targetSource = new SimpleContributionEndpointTargetSource();
	}

	/* *************** FactoryBean implementation methods ************** */

	public Object getObject() throws Exception {
		return proxyFactory.getProxy();
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		// no specific awareness of object type, so return null
		return null;
	}

	public boolean isSingleton() {
		// prototype currently not supported
		return true;
	}

	/* *************** dependency injection setters ************** */

	public void setProxyInterfaces(Class<?>[] interfaces) {
		this.interfaces = interfaces;
	}

	public void setTargetSource(ContributionEndpointTargetSource targetSource) {
		this.targetSource = targetSource;
	}
	
	public void setAllowNoService(boolean allowNoService) {
		this.allowNoService = allowNoService;
	}
	
	/* *************** ContributionEndpointTargetSource delegates ************** */

	public void registerTarget(String moduleName, Object bean) {
		targetSource.registerTarget(bean);
	}

	public void deregisterTarget(Object bean) {
		targetSource.deregisterTarget(bean);
	}

}
