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

package org.impalaframework.spring.module.registry;

import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.registry.Registry;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

public class NamedBeanRegistryContributor implements RegistryContributor, BeanFactoryAware, Ordered {

    private String registryBeanName;
    
    private Map<String, String> contributions;

    private BeanFactory beanFactory;
    
    private int order;
    
    /* ********** RegistryContributor implementation ********* */

    @SuppressWarnings("unchecked")
    public void doContributions() {
        Assert.notNull(beanFactory, "beanFactory cannot be null");
        Assert.notNull(contributions, "contributions cannot be null");
        
        Object registryBean = beanFactory.getBean(registryBeanName);
    
        final Registry registry = ObjectUtils.cast(registryBean, Registry.class);
        final Set<String> keys = contributions.keySet();
        
        for (String key : keys) {
                final String registrationKey = contributions.get(key);
                final Object bean = beanFactory.getBean(key);
                if (bean != null) {
                    try {
                        registry.addItem(registrationKey, bean);
                    } catch (ClassCastException e) {
                        throw new ConfigurationException("Bean '" + key + "' is not type compatible with " +
                                "registry bean '" + registryBeanName + "'");
                    }
                }
        }
    }
    
    /* ********** Order implementation ********* */

    public int getOrder() {
        return order;
    }

    /* ********** BeanFactoryAware implementation ********* */
    
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /* ********** Injection setters ********* */
    
    public void setRegistryBeanName(String registryBeanName) {
        this.registryBeanName = registryBeanName;
    }

    public void setContributions(Map<String, String> contributions) {
        this.contributions = contributions;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
