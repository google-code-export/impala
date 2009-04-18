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
import org.impalaframework.service.ContributionEndpoint;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.HierarchicalBeanFactory;

/**
 * <code>BeanPostProcessor</code> which attempts to register the created bean
 * with the parent's bean factory's <code>ContributionProxyFactoryBean</code>
 * 
 * @author Phil Zoio
 */
public abstract class ModuleContributionUtils {

    static ContributionEndpoint findContributionEndPoint(BeanFactory beanFactory, String beanName) {

        ContributionEndpoint factoryBean = null;
        if (beanFactory instanceof HierarchicalBeanFactory) {

            HierarchicalBeanFactory hierarchicalBeanFactory = (HierarchicalBeanFactory) beanFactory;
            BeanFactory parentBeanFactory = hierarchicalBeanFactory.getParentBeanFactory();

            if (parentBeanFactory != null) {

                String parentFactoryBeanName = "&" + beanName;

                try {

                    if (parentBeanFactory.containsBean(parentFactoryBeanName)) {
                        Object o = parentBeanFactory.getBean(parentFactoryBeanName);
                        if (o instanceof ContributionEndpoint) {
                            factoryBean = (ContributionEndpoint) o;
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
        return factoryBean;
    }

    static Object getTarget(Object bean, String beanName) {
        
        Object target = null;
        if (bean instanceof FactoryBean) {
            FactoryBean factoryBean = (FactoryBean) bean;
            try {
                target = factoryBean.getObject();
            }
            catch (Exception e) {
                String errorMessage = "Failed getting object from factory bean " + factoryBean + ", bean name "
                        + beanName;
                throw new BeanInstantiationException(factoryBean.getObjectType(), errorMessage, e);
            }
        }
        else {
            target = bean;
        }
        return target;
    }

    static BeanFactory getRootBeanFactory(BeanFactory beanFactory) {
        
        if (!(beanFactory instanceof HierarchicalBeanFactory)) {
            throw new ExecutionException(BeanFactory.class.getSimpleName() + " " + beanFactory + " is of type "
                    + beanFactory.getClass().getName() + ", which is not an instance of "
                    + HierarchicalBeanFactory.class.getName());
        }

        HierarchicalBeanFactory hierarchicalFactory = (HierarchicalBeanFactory) beanFactory;
        BeanFactory parentBeanFactory = hierarchicalFactory.getParentBeanFactory();

        if (parentBeanFactory != null) {
            beanFactory = getRootBeanFactory(parentBeanFactory);
        }
        return beanFactory;
    }

}
