/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.spring.dynamic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.PrototypeTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class DynamicBeansProcessor implements BeanFactoryPostProcessor {

	private static final Log logger = LogFactory.getLog(DynamicBeansProcessor.class);

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames().clone();
		for (String beanName : beanDefinitionNames) {
			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
			if (logger.isInfoEnabled())
				logger.info(beanName + ": " +  beanDefinition.getScope());

			if (beanDefinition.getScope().equals("dynamic") && beanFactory instanceof BeanDefinitionRegistry) {

				final BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) beanFactory;

				final String implBeanName = beanName + "Impl";
				final String targetSourceBeanName = beanName + "TargetSource";

				bdr.registerBeanDefinition(implBeanName, beanDefinition);

				/*
				 * <bean id="testInterface"
				 * class="org.springframework.aop.framework.ProxyFactoryBean">
				 * <property name="targetSource"> <bean
				 * class="org.impalaframework.spring.externalconfig.RefreshableTargetSourceFactoryBean">
				 * <property name="beanName" methodName = "testInterfaceImpl"/>
				 * </bean> </property> </bean>
				 */

				RootBeanDefinition targetSource = new RootBeanDefinition(PrototypeTargetSource.class);
				targetSource.getPropertyValues().addPropertyValue("targetBeanName", implBeanName);
				bdr.registerBeanDefinition(targetSourceBeanName, targetSource);

				RootBeanDefinition proxy = new RootBeanDefinition(ProxyFactoryBean.class);
				proxy.getPropertyValues().addPropertyValue("targetSource",
						new RuntimeBeanReference(targetSourceBeanName));
				bdr.registerBeanDefinition(beanName, proxy);

			}
		}
	}

}
