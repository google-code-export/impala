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

package org.impalaframework.spring.service.bean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.impalaframework.service.ServiceBeanReference;
import org.impalaframework.service.ServiceRegistryEntry;

/**
 * Implementation of {@link ServiceRegistryEntry} which does not actually go into service registry, and is
 * hence local only to the module itself.
 * @author Phil Zoio
 */
public class LocalOnlyServiceRegistryEntry implements ServiceRegistryEntry {

    private ServiceBeanReference serviceBeanReference;
    
    private ClassLoader beanClassLoader;
    
    private String beanName;    
    
    private String contributingModule;
    
    public LocalOnlyServiceRegistryEntry(
            ServiceBeanReference serviceBeanReference, 
            String beanName,
            String contributingModule, 
            ClassLoader beanClassLoader) {
        super();
        this.serviceBeanReference = serviceBeanReference;
        this.beanName = beanName;
        this.contributingModule = contributingModule;
        this.beanClassLoader = beanClassLoader;
    }


    public Map<String, ?> getAttributes() {
        return Collections.emptyMap();
    }

    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getContributingModule() {
        return contributingModule;
    }

    public List<Class<?>> getExportTypes() {
        return Collections.emptyList();
    }

    public ServiceBeanReference getServiceBeanReference() {
        return serviceBeanReference;
    }

}
