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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.service.ContributionEndpoint;
import org.impalaframework.spring.service.proxy.ContributionProxyFactoryBean;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * Subclass of <code>BaseModuleContributionExporter</code> which will automatically register
 * <code>ContributionEndPoints</code> in the root module application context for each of the
 * contributions named using the <code>contributions</code> property, if such a <code>ContributionEndPoint</code>
 * does not already exist. Note that the interface names need to be specified using the value portion of 
 * each contribution entry. The key portion is used to specify the bean name to be used.
 * @see ModuleArrayContributionExporter
 * 
 * @author Phil Zoio
 */
public class AutoRegisteringModuleContributionExporter extends BaseModuleContributionExporter implements
        BeanClassLoaderAware {

    private Map<String, String> contributions;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(contributions, "contributions cannot be null");

        Set<String> beanNames = contributions.keySet();
        processContributions(beanNames);
    }

    protected ContributionEndpoint getContributionEndPoint(String beanName, Object bean) {
        ContributionEndpoint endPoint = ModuleContributionUtils.findContributionEndPoint(getBeanFactory(), beanName);

        if (endPoint == null) {
            String contributionClassNames = contributions.get(beanName);
            checkContributionClasses(bean, beanName, contributionClassNames);
            
            RootBeanDefinition beanDefinition = new RootBeanDefinition(ContributionProxyFactoryBean.class);
            beanDefinition.getPropertyValues().addPropertyValue("proxyInterfaces", contributionClassNames);
            
            BeanFactory rootBeanFactory = ModuleContributionUtils.getRootBeanFactory(getBeanFactory());

            BeanDefinitionRegistry registry = getBeanDefinitionRegistry(rootBeanFactory);
            registry.registerBeanDefinition(beanName, beanDefinition);

            endPoint = (ContributionEndpoint) rootBeanFactory.getBean("&" + beanName,ContributionEndpoint.class);
        }

        return endPoint;
    }

    BeanDefinitionRegistry getBeanDefinitionRegistry(BeanFactory rootBeanFactory) {
        if (!(rootBeanFactory instanceof BeanDefinitionRegistry)) {
            throw new ExecutionException("Cannot use " + this.getClass().getName() + " with bean factory which does not implement " + BeanDefinitionRegistry.class.getName());
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) rootBeanFactory;
        return registry;
    }

    @SuppressWarnings("unchecked")
    void checkContributionClasses(Object bean, String beanName, String typeList) {
        String[] interfaces = typeList.split(",");

        List<Class> interfaceClasses = new ArrayList<Class>();
        for (String interfaceClass : interfaces) {
            Class resolvedClassName = ClassUtils.resolveClassName(interfaceClass.trim(), getBeanClassLoader());

            if (!resolvedClassName.isAssignableFrom(bean.getClass())) {
                throw new ExecutionException("Bean '" + beanName + "' is not instance of type " + resolvedClassName.getName() + ", declared in type list '" + typeList + "'");
            }

            interfaceClasses.add(resolvedClassName);
        }
    }

    public void setContributions(Map<String, String> contributions) {
        this.contributions = contributions;
    }

}
