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

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.service.ServiceBeanReference;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * Represents service bean backed by static bean
 * @author Phil Zoio
 */
public class StaticSpringServiceBeanReference implements ServiceBeanReference {

    private final Object service;
    
    public StaticSpringServiceBeanReference(Object service) {
        super();
        Assert.notNull(service);
        if (service instanceof FactoryBean) {
            FactoryBean factoryBean = (FactoryBean)service;
            try {
                this.service = factoryBean.getObject();
            }
            catch (Exception e) {
                throw new ExecutionException("Error retrieving target object from factory bean " + factoryBean, e);
            }
        } else {
            this.service = service;
        }
    }

    public Object getService() {
        return service;
    }

    public boolean isStatic() {
        return true;
    }

}
