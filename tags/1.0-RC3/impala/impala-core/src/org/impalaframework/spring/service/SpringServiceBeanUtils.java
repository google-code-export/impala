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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceBeanReference;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.Assert;

/**
 * {@link FactoryBean} which simply injects bean obtained from parent bean factory
 * @author Phil Zoio
 */
public class SpringServiceBeanUtils {
    
    private static final Log logger = LogFactory.getLog(SpringServiceBeanUtils.class);

    /**
     * Returns instance of {@link StaticSpringServiceBeanReference} if bean is represented by a singleton
     * and {@link SpringServiceBeanReference} if not.
     * @see SpringServiceBeanUtils#isSingleton(BeanFactory, String)
     */
    public static ServiceBeanReference newServiceBeanReference(BeanFactory beanFactory, String beanName) {
    
        Object bean = beanFactory.getBean(beanName);
        boolean singleton = SpringServiceBeanUtils.isSingleton(beanFactory, beanName);
        if (singleton) {
            return new StaticSpringServiceBeanReference(bean);
        }
        return new SpringServiceBeanReference(beanFactory, beanName);
    }

    /**
     * Checks that the bean with given name contained in the specified bean factory is a singleton.
     * Will return true if bean represented by a bean registered under the scope <code>singletone</code>.
     * If the bean is also a factory bean (see {@link FactoryBean}), then the {@link FactoryBean}
     * instance also needs to be a singleton.
     * 
     * Note that in order to work properly the {@link BeanFactory} must be able to recover the {@link BeanDefinition}
     * for a particular bean name. This in particular must mean implementing {@link BeanDefinitionRegistry} or 
     * {@link BeanDefinitionExposing}. In the latter case, if null is returned, then the bean will be treated as a singleton.
     * 
     * @return true if bean is singleton registered bean and, if applicable, a singleton {@link FactoryBean}.
     */
    public static boolean isSingleton(
            BeanFactory beanFactory,
            String beanName) {
    
        Assert.notNull(beanFactory, "beanFactory cannot be null");
        Assert.notNull(beanName, "beanName cannot be null");
    
        boolean singleton = true;
    
        boolean isBeanFactory = beanFactory.containsBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName);
        if (isBeanFactory) {
            FactoryBean factoryBean = (FactoryBean) beanFactory.getBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName);
            singleton = factoryBean.isSingleton();
        }
        
        if (singleton) {
            //ApplicationContext implements this implements this
            ListableBeanFactory registry = (ListableBeanFactory) beanFactory;
            
            //we're only interested in top level definitions
            //inner beans won't appear here, so 
            boolean containsBeanDefinition = registry.containsBeanDefinition(beanName);
            if (containsBeanDefinition) {
                BeanDefinition beanDefinition = getBeanDefinition(registry, beanName);
                
                if (beanDefinition != null) {
                    singleton = beanDefinition.isSingleton();
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Cannot check whether bean definition " + beanName + " is singleton as it is not available as a top level bean");
                }
            }
        }
        return singleton;
    }

    static BeanDefinition getBeanDefinition(BeanFactory beanFactory, String beanName) {
        if (beanFactory instanceof BeanDefinitionRegistry) {
            return ((BeanDefinitionRegistry) beanFactory).getBeanDefinition(beanName);
        }
        if (beanFactory instanceof BeanDefinitionExposing) {
            BeanDefinition beanDefinition = ((BeanDefinitionExposing) beanFactory).getBeanDefinition(beanName);
            return beanDefinition;
        }
        
        throw new InvalidStateException("Cannot get bean definition as bean factory [" 
                + beanFactory.getClass().getName()
                + "] does not implement [" + BeanDefinitionRegistry.class
                + "] or " + BeanDefinitionExposing.class.getName() + "]");
    }
    
    

}
