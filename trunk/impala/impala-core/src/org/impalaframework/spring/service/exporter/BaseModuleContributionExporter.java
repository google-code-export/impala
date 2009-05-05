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

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.service.ContributionEndpoint;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Implements base functionality to register a set of named contributions with
 * the <code>ServiceRegistry</code>.
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleContributionExporter implements ModuleDefinitionAware, BeanFactoryAware,
        InitializingBean, DisposableBean, ServiceRegistryAware, BeanClassLoaderAware {

    private static final Log logger = LogFactory.getLog(BaseModuleContributionExporter.class);

    private BeanFactory beanFactory;

    private ModuleDefinition moduleDefinition;
    
    private ServiceRegistry serviceRegistry;
    
    private ClassLoader beanClassLoader;

    private Map<ServiceRegistryEntry, ContributionEndpoint> contributionMap = new IdentityHashMap<ServiceRegistryEntry, ContributionEndpoint>();

    /**
     * This implementation will only add an entry to the {@link ServiceRegistry}
     * if it can find a {@link ContributionEndpoint} in a super-
     * {@link org.springframework.context.ApplicationContext}
     * which has the same name as the name of the bean
     */
    protected final void processContributions(Collection<String> contributions) {
        for (String beanName : contributions) {

            Object bean = beanFactory.getBean(beanName);

            ContributionEndpoint endPoint = getContributionEndPoint(beanName, bean);

            //if contribution endpoint exists corresponding with bean name, then we add
            //to the contribution map, and register the bean
            if (endPoint != null) {
                
                if (serviceRegistry != null) {
                    String moduleName = moduleDefinition.getName();
                    logger.info("Contributing bean " + beanName + " from module " + moduleName);
                    final ServiceRegistryEntry serviceReference = serviceRegistry.addService(beanName, moduleName, bean, beanClassLoader);
                    contributionMap.put(serviceReference, endPoint);
                }   
            }       
        }
    }

    public void destroy() throws Exception {
        Set<ServiceRegistryEntry> contributionKeys = contributionMap.keySet();
        
        //go through the contributions and remove
        for (ServiceRegistryEntry reference : contributionKeys) {           
            if (serviceRegistry != null) {
                serviceRegistry.remove(reference);
            }
        }
    }

    protected ContributionEndpoint getContributionEndPoint(String beanName, Object bean) {
        return ModuleContributionUtils.findContributionEndPoint(beanFactory, beanName);
    }

    protected BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setModuleDefinition(ModuleDefinition moduleDefinition) {
        this.moduleDefinition = moduleDefinition;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

}
