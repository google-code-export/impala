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

package org.impalaframework.spring.service.proxy;

import java.util.List;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.contribution.BaseServiceRegistryList;
import org.impalaframework.service.reference.BasicServiceRegistryReference;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.impalaframework.spring.service.registry.BaseServiceRegistryTargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;

/**
 * The <code>ServiceProxyFactoryBean</code> TBD
 * 
 * @see BasicServiceRegistryReference
 * @author Phil Zoio
 */
public class ServiceProxyFactoryBean extends BaseContributionProxyFactoryBean implements BeanNameAware, DisposableBean {

    private static final long serialVersionUID = 1L;

    private Class<?>[] proxyInterfaces;

    private String beanName;
    
    //FIXME use this
    private String exportedBeanName;

    private String filterExpression;

    private ServiceRegistryList list;

    /* *************** BeanNameAware implementation method ************** */

    public void setBeanName(String name) {
        this.beanName = name;
    }

    /* *************** Abstract superclass method implementation ************** */

    protected ProxyFactory createProxyFactory() {
        
        list = new ServiceRegistryList();
        list.setServiceRegistry(getServiceRegistry());
        list.setFilterExpression(filterExpression);
        list.init();
        ListBackedProxySource source = new ListBackedProxySource(list, proxyInterfaces);
        
        ProxyFactory createDynamicProxyFactory = getProxyFactoryCreator().createProxyFactory(source, beanName);
        return createDynamicProxyFactory;
    }
    
    /* *************** dependency injection setters ************** */
    
    public void destroy() throws Exception {
        list.destroy();
    }

    /* *************** dependency injection setters ************** */

    public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }

    public void setExportedBeanName(String exportedBeanName) {
        this.exportedBeanName = exportedBeanName;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    ServiceRegistryList getList() {
        return list;
    }

}

class ListBackedProxySource extends BaseProxyFactorySource {

    private ServiceRegistryList list;

    private Class<?>[] proxyInterfaces;

    public ListBackedProxySource(ServiceRegistryList list,
            Class<?>[] proxyInterfaces) {
        super();
        this.list = list;
        this.proxyInterfaces = proxyInterfaces;
    }

    public void init() {
        ContributionEndpointTargetSource targetSource = new ListBackedRegistryTargetSource(this.list);
        ProxyFactory proxyFactory = new ProxyFactory();
        
        if (targetSource.getTargetClass() == null) {
            //not proxying by class, so proxy by interface
            ProxyFactorySourceUtils.addInterfaces(proxyFactory, proxyInterfaces);
        }
        
        afterInit(proxyFactory, targetSource);
    }
}


class ServiceRegistryList extends BaseServiceRegistryList {

    @Override
    protected Object maybeGetProxy(ServiceRegistryReference ref) {
        return ref.getBean();
    }

    @Override
    public List<ServiceRegistryReference> getServices() {
        return super.getServices();
    }

}

class ListBackedRegistryTargetSource extends BaseServiceRegistryTargetSource {

    private ServiceRegistryList list;

    public ListBackedRegistryTargetSource(
            ServiceRegistryList serviceRegistryList) {
        super();
        list = serviceRegistryList;
    }

    public ServiceRegistryReference getServiceRegistryReference() {
        List<ServiceRegistryReference> contributions = list.getServices();
        if (contributions.size() > 0) {
            return contributions.get(0);
        }
        return null;
    }

}