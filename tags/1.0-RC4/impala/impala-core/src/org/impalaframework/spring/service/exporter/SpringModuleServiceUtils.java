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

package org.impalaframework.spring.service.exporter;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.service.NamedServiceEndpoint;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.HierarchicalBeanFactory;

/**
 * 
 * @author Phil Zoio
 */
public abstract class SpringModuleServiceUtils {

     /**
     * If parent bean factory contains bean of the same name which is a {@link FactoryBean}
     * implementing the {@link NamedServiceEndpoint} interface, then this instance will be returned.
     * Otherwise, returns null.
     * @param beanFactory the current bean's bean factory
     * @param beanName the name of the bean to check
     * @return {@link NamedServiceEndpoint} if present, otherwise null.
     */
    static NamedServiceEndpoint findServiceEndpoint(
            BeanFactory beanFactory, String beanName) {

        NamedServiceEndpoint endpoint = null;

        //continue looping until you find an endpoint or have no more parents to check through
        while (beanFactory != null && endpoint == null) {

            if (beanFactory instanceof HierarchicalBeanFactory) {

                HierarchicalBeanFactory hierarchicalBeanFactory = (HierarchicalBeanFactory) beanFactory;
                beanFactory = hierarchicalBeanFactory.getParentBeanFactory();

                endpoint = getEndpoint(beanFactory, beanName);
            }

        }
        return endpoint;
    }

    /**
     * Returns {@link NamedServiceEndpoint} from specified {@link BeanFactory} and bean name
     * (qualified using the factory bean prefix &), assuming this exists.
     */
    private static NamedServiceEndpoint getEndpoint(BeanFactory beanFactory,
            String beanName) {
        
        NamedServiceEndpoint endpoint = null;
        if (beanFactory != null) {
      
            String parentFactoryBeanName = "&" + beanName;
      
            try {
      
                if (beanFactory.containsBean(parentFactoryBeanName)) {
                    Object o = beanFactory.getBean(parentFactoryBeanName);
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
        return endpoint;
    }

    /**
     * Recursive method to return the root bean factory for the current bean factory.
     * Relies on children {@link BeanFactory}s implementing the {@link HierarchicalBeanFactory}
     * interface
     */
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
