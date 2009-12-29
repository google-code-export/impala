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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Base class for exposing an existing bean
 * @author Phil Zoio
 */
public abstract class BaseExistingBeanExposingFactoryBean implements InitializingBean, FactoryBean, BeanFactoryAware, ApplicationContextAware {

    private BeanFactory beanFactory;
    
    private ApplicationContext applicationContext;

    /**
     * Whether to include the current bean definition when searching for the bean factory
     */
    protected abstract boolean getIncludeCurrentBeanFactory();

    /**
     * Returns the bean name to search for
     */
    protected abstract String getBeanNameToSearchFor();
    
    /**
     * Finds the first parent {@link BeanFactory} which contains a bean of the given name
     */
    BeanFactory findBeanFactory() {

        final BeanFactory beanFactory = maybeFindBeanFactory();
        
        if (beanFactory != null) {
            return beanFactory;
        }
        
        throw new BeanDefinitionValidationException("No parent bean factory of application context [" + applicationContext.getDisplayName() + "] contains bean [" + getBeanNameToSearchFor() + "]");
    }

    protected BeanFactory maybeFindBeanFactory() {
        
        BeanFactory currentBeanFactory = this.beanFactory;
        
        if (getIncludeCurrentBeanFactory()) {
            if (this.beanFactory instanceof ListableBeanFactory) {
                if (((ListableBeanFactory) this.beanFactory).containsBeanDefinition(getBeanNameToSearchFor())) {
                    return this.beanFactory;
                }
            }
        }
        
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
                    if (listable.containsBeanDefinition(getBeanNameToSearchFor())) {
                        return currentBeanFactory;
                    }
                }
            }
        }
        
        return null;
    }

    public Class<?> getObjectType() {
        return null;
    }
    
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
