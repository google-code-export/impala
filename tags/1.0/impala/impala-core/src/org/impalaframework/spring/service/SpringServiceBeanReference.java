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

package org.impalaframework.spring.service;

import org.impalaframework.service.ServiceBeanReference;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;

/**
 * Represents non-static Spring bean reference
 * @author Phil Zoio
 */
public class SpringServiceBeanReference implements ServiceBeanReference {

    private final String beanName;
    private final BeanFactory beanFactory;
    
    public SpringServiceBeanReference(BeanFactory beanFactory, String beanName) {
        super();
        Assert.notNull(beanName);
        Assert.notNull(beanFactory);
        
        this.beanFactory = beanFactory;
        this.beanName = beanName;
    }

    public Object getService() {
        return beanFactory.getBean(beanName);
    }

    public boolean isStatic() {
        return false;
    }

}
