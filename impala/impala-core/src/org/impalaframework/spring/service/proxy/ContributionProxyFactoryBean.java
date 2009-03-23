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

package org.impalaframework.spring.service.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ContributionEndpoint;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.impalaframework.spring.service.registry.ServiceRegistryTargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;

/**
 * <code>FactoryBean</code> which creates a proxy which has uses
 * <code>ServiceRegistryTargetSource</code> as a target source and
 * <code>ContributionEndpointInterceptor</code> as interceptor
 * 
 * @author Phil Zoio
 */
public class ContributionProxyFactoryBean implements FactoryBean, BeanNameAware, InitializingBean, ContributionEndpoint, ServiceRegistryAware, BeanClassLoaderAware {

	private static final Log logger = LogFactory.getLog(ContributionProxyFactoryBean.class);

	private static final long serialVersionUID = 1L;

	private Class<?>[] interfaces;

	private String beanName;
	
	private String exportedBeanName;
	
	private ServiceRegistry serviceRegistry;

	private ProxyFactory proxyFactory;

	private ContributionEndpointTargetSource targetSource;
	
	private boolean allowNoService;
	
	/**
	 * Whether to set the context class loader to the class loader of the module
	 * contributing the bean being invoked. This is to mitigate possibility of
	 * exceptions being caused by calls to <code>Thread.setContextClassLoader()</code> being
	 * propagated across modules
	 */
	private boolean setContextClassLoader = true;

	private ClassLoader beanClassLoader;

	/* *************** BeanNameAware implementation method ************** */

	public void setBeanName(String name) {
		this.beanName = name;
	}

	/* *************** InitializingBean implementation method ************** */

	public void afterPropertiesSet() throws Exception {
		
		String registryKeyName = (exportedBeanName != null ? exportedBeanName : beanName);
		targetSource = new ServiceRegistryTargetSource(registryKeyName, serviceRegistry);
		
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
		interceptor.setSetContextClassLoader(setContextClassLoader);
		proxyFactory.addAdvice(interceptor);
	}

	/* *************** FactoryBean implementation methods ************** */

	public Object getObject() throws Exception {
		return proxyFactory.getProxy(beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader());
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
	
	public void setAllowNoService(boolean allowNoService) {
		this.allowNoService = allowNoService;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	public void setExportedBeanName(String exportedBeanName) {
		this.exportedBeanName = exportedBeanName;
	}
	
	public void setSetContextClassLoader(boolean setContextClassLoader) {
		this.setContextClassLoader = setContextClassLoader;
	}
	
	/* *************** ServiceRegistryAware implementation ************** */
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
	/* *************** ContributionEndpointTargetSource delegates ************** */

	public void registerTarget(String moduleName, Object bean) {
	}

	public void deregisterTarget(Object bean) {
	}

}
