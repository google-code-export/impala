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

package org.impalaframework.spring.service.exporter;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.service.NamedServiceEndpoint;
import org.impalaframework.service.ServiceBeanReference;
import org.impalaframework.spring.service.SpringServiceBeanReference;
import org.impalaframework.spring.service.StaticSpringServiceBeanReference;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.util.Assert;

/**
 * 
 * @author Phil Zoio
 */
public abstract class SpringModuleServiceUtils {
    
    static ServiceBeanReference newServiceBeanReference(BeanFactory beanFactory, String beanName) {

        Object bean = beanFactory.getBean(beanName);
        boolean singleton = isSingleton(beanFactory, beanName, bean);
        if (singleton) {
            return new StaticSpringServiceBeanReference(bean);
        }
        return new SpringServiceBeanReference(beanFactory, beanName);
    }

    public static boolean isSingleton(
            BeanFactory beanFactory,
            String beanName, 
            Object bean) {

        Assert.notNull(beanFactory, "beanFactory cannot be null");
        Assert.notNull(beanName, "beanName cannot be null");
        Assert.notNull(bean, "bean cannot be null");

        boolean singleton = true;
        
        if (bean instanceof FactoryBean) {
            FactoryBean factoryBean = (FactoryBean) bean;
            singleton = factoryBean.isSingleton();
        }
        
        if (singleton) {
            singleton = beanFactory.isSingleton(beanName);
        }
        return singleton;
    }

    static NamedServiceEndpoint findServiceEndpoint(
            BeanFactory beanFactory, String beanName) {

        NamedServiceEndpoint endpoint = null;
        if (beanFactory instanceof HierarchicalBeanFactory) {

            HierarchicalBeanFactory hierarchicalBeanFactory = (HierarchicalBeanFactory) beanFactory;
            BeanFactory parentBeanFactory = hierarchicalBeanFactory
                    .getParentBeanFactory();

            if (parentBeanFactory != null) {

                String parentFactoryBeanName = "&" + beanName;

                try {

                    if (parentBeanFactory.containsBean(parentFactoryBeanName)) {
                        Object o = parentBeanFactory
                                .getBean(parentFactoryBeanName);
                        if (o instanceof NamedServiceEndpoint) {
                            endpoint = (NamedServiceEndpoint) o;
                        }
                    }
                }
                catch (BeanIsNotAFactoryException e) {
                    // This is check is only present due to a bug in an early
                    // 2.0
                    // release, which was fixed certainly before 2.0.6
                    // ordinarily, this exception will never be thrown
                }
            }
        }
        return endpoint;
    }

    static BeanFactory getRootBeanFactory(BeanFactory beanFactory) {

        if (!(beanFactory instanceof HierarchicalBeanFactory)) {
            throw new ExecutionException(BeanFactory.class.getSimpleName()
                    + " " + beanFactory + " is of type "
                    + beanFactory.getClass().getName()
                    + ", which is not an instance of "
                    + HierarchicalBeanFactory.class.getName());
        }

        HierarchicalBeanFactory hierarchicalFactory = (HierarchicalBeanFactory) beanFactory;
        BeanFactory parentBeanFactory = hierarchicalFactory
                .getParentBeanFactory();

        if (parentBeanFactory != null) {
            beanFactory = getRootBeanFactory(parentBeanFactory);
        }
        return beanFactory;
    }

}
