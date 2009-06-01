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

package org.impalaframework.spring.service.bean;

import org.impalaframework.service.ServiceBeanReference;
import org.impalaframework.spring.service.SpringServiceBeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * {@link FactoryBean} which simply injects bean obtained from parent bean factory.
 * @author Phil Zoio
 */
public class ParentFactoryBean implements InitializingBean, FactoryBean, BeanNameAware, BeanFactoryAware, ApplicationContextAware {

    private String beanName;
    
    private BeanFactory beanFactory;
    
    private ApplicationContext applicationContext;
    
    private ServiceBeanReference serviceBeanReference;
    
    /**
     * Finds the first parent {@link BeanFactory} which contains a bean of the given name
     */
    BeanFactory findParentFactory() {

        BeanFactory currentBeanFactory = this.beanFactory;
        
        //continue looping until you find a parent bean factory which contains the given bean
        while (currentBeanFactory != null) {

            if (currentBeanFactory instanceof HierarchicalBeanFactory) {
                HierarchicalBeanFactory hierarchicalBeanFactory = (HierarchicalBeanFactory) currentBeanFactory;
                currentBeanFactory = hierarchicalBeanFactory.getParentBeanFactory();               
            } else {
                currentBeanFactory = null;
            }
            
            if (currentBeanFactory != null) {
                if (currentBeanFactory instanceof ListableBeanFactory) {
                    ListableBeanFactory listable = (ListableBeanFactory) currentBeanFactory;
                    if (listable.containsBeanDefinition(this.beanName)) {
                        return currentBeanFactory;
                    }
                }
            }
        }
        return null;
    }

    public void afterPropertiesSet() throws Exception {
        BeanFactory parentFactory = findParentFactory();
        
        if (parentFactory == null) {
            throw new BeanDefinitionValidationException("No parent bean factory of application context [" + applicationContext.getDisplayName() + "] contains bean [" + beanName + "]");
        }
        
        serviceBeanReference = SpringServiceBeanUtils.newServiceBeanReference(parentFactory, this.beanName);
    }

    public Object getObject() throws Exception {
        return serviceBeanReference.getService();
    }

    public Class<?> getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return serviceBeanReference.isStatic();
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


}
