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

import org.impalaframework.module.definition.ModuleDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

/**
 * <code>BeanPostProcessor</code> which attempts to register the created bean
 * with the parent's bean factory's <code>ContributionProxyFactoryBean</code>
 * 
 * @author Phil Zoio
 */
public class ModuleContributionPostProcessor implements ModuleDefinitionAware, BeanPostProcessor, BeanFactoryAware,
		DestructionAwareBeanPostProcessor {

	private static final Log logger = LogFactory.getLog(ModuleContributionPostProcessor.class);

	private BeanFactory beanFactory;

	private Object target;

	private ModuleDefinition moduleDefinition;

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		ContributionEndpoint endPoint = ModuleContributionUtils.findContributionEndPoint(beanFactory, beanName);
		if (endPoint != null) {

			target = ModuleContributionUtils.getTarget(bean, beanName);

			String moduleName = null;
			if (moduleDefinition != null) {
				logger.info("Contributing bean " + beanName + " from module " + moduleDefinition.getName());
				moduleName = moduleDefinition.getName();
			}
			endPoint.registerTarget(moduleName, target);
		}

		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		ContributionEndpoint factoryBean = ModuleContributionUtils.findContributionEndPoint(beanFactory, beanName);
		if (factoryBean != null) {
			factoryBean.deregisterTarget(bean);
		}
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setModuleDefinition(ModuleDefinition moduleDefinition) {
		this.moduleDefinition = moduleDefinition;
	}

}
