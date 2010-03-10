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

package org.impalaframework.spring.service.bean;

import org.impalaframework.service.ServiceBeanReference;
import org.impalaframework.spring.service.SpringServiceBeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * {@link FactoryBean} which simply injects bean obtained from parent bean
 * factory. This implementation is functionally equivalent to {@link org.impalaframework.spring.bean.NamedFactoryBean}
 * for singletons, but also is able to detect correctly when the bean that it is exposing is a singleton, and
 * return {@link #isSingleton()} accordingly. If this functionality is not required, and you know that
 * the bean you are exposing is a singleton, consider using {@link org.impalaframework.spring.bean.NamedFactoryBean} instead of this.
 * 
 * @see org.impalaframework.spring.bean.NamedFactoryBean
 * @author Phil Zoio
 */
public class SingletonAwareNamedFactoryBean extends BaseExistingBeanExposingFactoryBean {

    private String beanName;
    
    private ServiceBeanReference serviceBeanReference;

    @Override
    protected String getBeanNameToSearchFor() {
        return beanName;
    }

    @Override
    protected boolean getIncludeCurrentBeanFactory() {
        return true;
    }    
    
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(beanName, "beanName cannot be null");
        
        BeanFactory parentFactory = findBeanFactory();
        serviceBeanReference = SpringServiceBeanUtils.newServiceBeanReference(parentFactory, getBeanNameToSearchFor());
    }

    public Object getObject() throws Exception {
        return serviceBeanReference.getService();
    }

    public boolean isSingleton() {
        return serviceBeanReference.isStatic();
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
