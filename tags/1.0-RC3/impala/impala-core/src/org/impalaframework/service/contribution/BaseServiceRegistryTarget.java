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

package org.impalaframework.service.contribution;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEvent;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.util.Assert;

/**
 * Implements basic operations required for supporting dynamic interaction with a {@link ServiceRegistry}.
 * 
 * @author Phil Zoio
 */
public abstract class BaseServiceRegistryTarget implements 
    ServiceRegistryEventListener,
    ServiceRegistryAware, 
    ServiceActivityNotifiable
    {
    
    /**
     * Used to simplify interactions with the {@link ServiceRegistry}
     */
    private ServiceRegistryMonitor serviceRegistryMonitor;
    
    /**
     * Source of map contributions
     */
    private ServiceRegistry serviceRegistry;
    
    /**
     * Filter expression used to retrieve services which are eligible to be added
     * as a contribution to this map.
     */
    private String filterExpression;
    
    /**
     * Filter used to retrieve services which are eligible to be added
     * as a contribution to this map.
     */
    private ServiceReferenceFilter filter;    
    
    /**
     * Sets the implementation types which must be matched against before service can be 
     * passed to {@link ServiceActivityNotifiable} instance.
     */
    private Class<?>[] proxyTypes;
    
    private Class<?>[] exportTypes;
    
    public BaseServiceRegistryTarget() {
        super();
    }
    
    /* **************** Life cycle methods *************** */
    
    public void init() {
        Assert.notNull(this.serviceRegistry, "serviceRegistry cannot be null");

        if (this.filter == null) {
            Assert.notNull(this.filterExpression, "filterExpression and filter both cannot be null");
            this.filter = new LdapServiceReferenceFilter(filterExpression);
        }

        this.serviceRegistryMonitor = new ServiceRegistryMonitor();
        this.serviceRegistryMonitor.setServiceRegistry(serviceRegistry);
        this.serviceRegistryMonitor.setServiceActivityNotifiable(this);
        this.serviceRegistryMonitor.init();
        this.serviceRegistry.addEventListener(this);
    }
    
    public void destroy() {
        this.serviceRegistry.removeEventListener(this);
    }
    
    /* ******************* (Partial) implementation of ServiceRegistryNotifiable ******************** */
    
    public ServiceReferenceFilter getServiceReferenceFilter() {
        return filter;
    }

    public Class<?>[] getProxyTypes() {
        return proxyTypes;
    }   
    
    public Class<?>[] getExportTypes() {
        return exportTypes;
    }

    public boolean getAllowNonStaticReferences() {
        return false;
    }

    /* ******************* Implementation of ServiceRegistryEventListener ******************** */

    public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
        serviceRegistryMonitor.handleServiceRegistryEvent(event);
    }

    /* ******************* ServiceRegistryAware implementation ******************** */
    
    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }   

    /* ******************* Protected getters ******************** */
    
    protected ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }
    
    protected ServiceReferenceFilter getFilter() {
        return filter;
    }
    
    /* ******************* Injected setters ******************** */
    
    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }
    
    public void setFilter(ServiceReferenceFilter filter) {
        this.filter = filter;
    }

    public void setServiceRegistryMonitor(ServiceRegistryMonitor serviceRegistryMonitor) {
        this.serviceRegistryMonitor = serviceRegistryMonitor;
    }

    public void setProxyTypes(Class<?>[] proxyTypes) {
        this.proxyTypes = proxyTypes;
    }

    public void setExportTypes(Class<?>[] exportTypes) {
        this.exportTypes = exportTypes;
    }
    
}
