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

package org.impalaframework.spring.service.exporter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.service.ContributionEndpoint;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

/**
 * <code>BeanPostProcessor</code> which attempts to register the created bean
 * with the <code>ServiceRegistry</code>, but only if the current bean's parent context
 * has a same-named bean which implements <code>ContributionEndPoint</code>.
 * 
 * @author Phil Zoio
 */
public class ModuleContributionPostProcessor implements ModuleDefinitionAware, ServiceRegistryAware, BeanPostProcessor, BeanFactoryAware,
		DestructionAwareBeanPostProcessor, BeanClassLoaderAware {

	private static final Log logger = LogFactory.getLog(ModuleContributionPostProcessor.class);

	private BeanFactory beanFactory;
	
	private ServiceRegistry serviceRegistry;

	private ModuleDefinition moduleDefinition;
	
	private ClassLoader beanClassLoader;

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		String moduleName = moduleName();
		
		//only if there is a contribution end point corresponding with bean name do we register the service
		ContributionEndpoint endPoint = ModuleContributionUtils.findContributionEndPoint(beanFactory, beanName);
		if (endPoint != null) {			
			logger.info("Contributing bean " + beanName + " from module " + moduleName);
			
			if (serviceRegistry != null) {
				serviceRegistry.addService(beanName, moduleName, bean, beanClassLoader);
			} else {
				logger.warn("Could not contribute bean " + beanName + " from module " + moduleName + " as service registry is null");
			}
		}	

		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
	
		//remove bean if end point exists corresponding with bean name
		ContributionEndpoint endPoint = ModuleContributionUtils.findContributionEndPoint(beanFactory, beanName);
		if (endPoint != null) {
			if (serviceRegistry != null) {
				serviceRegistry.remove(bean);
			} else {
				logger.warn("Could not remove bean " + beanName + " from service registry as this is null");
			}
		}
	}
	
	private String moduleName() {
		String moduleName = null;
		if (moduleDefinition != null) {
			moduleName = moduleDefinition.getName();
		}
		return moduleName;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setModuleDefinition(ModuleDefinition moduleDefinition) {
		this.moduleDefinition = moduleDefinition;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

}
