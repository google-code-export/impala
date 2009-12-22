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
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;

/**
 * {@link FactoryBean} which simply injects bean obtained from parent bean
 * factory.
 * @author Phil Zoio
 */
public class ParentFactoryBean extends BaseExistingBeanExposingFactoryBean
        implements BeanNameAware {

    private String beanName;
    
    private ServiceBeanReference serviceBeanReference;

    @Override
    protected String getBeanNameToSearchFor() {
        return beanName;
    }

    @Override
    protected boolean getIncludeCurrentBeanFactory() {
        return false;
    }    
    
    public void afterPropertiesSet() throws Exception {
        BeanFactory parentFactory = findBeanFactory();
       
        serviceBeanReference = SpringServiceBeanUtils.newServiceBeanReference(parentFactory, getBeanNameToSearchFor());
    }

    public Object getObject() throws Exception {
        return serviceBeanReference.getService();
    }

    public boolean isSingleton() {
        return serviceBeanReference.isStatic();
    }

    /**
     * This will be called by the container after {@link #setParentBeanName(String)}.
     * Thus if value is set using {@link #setParentBeanName(String)}, then this method will have no effect
     */
    public void setBeanName(String beanName) {
        if (this.beanName == null) {
            this.beanName = beanName;
        }
    }
    
    /* ******************** Injected setters ******************** */
    
    public void setParentBeanName(String beanName) {
        this.beanName = beanName;
    }    

}
