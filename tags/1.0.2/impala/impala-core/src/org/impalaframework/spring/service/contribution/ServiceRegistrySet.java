package org.impalaframework.spring.service.contribution;

import java.util.Set;

import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.contribution.BaseServiceRegistrySet;
import org.impalaframework.spring.service.ProxyFactoryCreator;
import org.impalaframework.spring.service.SpringServiceEndpoint;
import org.impalaframework.spring.service.proxy.DefaultProxyFactoryCreator;
import org.impalaframework.spring.service.proxy.ProxyFactoryCreatorAware;
import org.impalaframework.spring.service.proxy.StaticServiceReferenceProxyFactorySource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Spring-based service registry {@link Set} implementation which uses a possibly wired in 
 * {@link ProxyFactoryCreator} to create a proxy for the backed object.
 * 
 * @see ServiceRegistryList
 * @see ServiceRegistryMap
 * @author Phil Zoio
 */
public class ServiceRegistrySet extends BaseServiceRegistrySet
        implements InitializingBean, DisposableBean, BeanNameAware, ProxyFactoryCreatorAware, SpringServiceEndpoint {

    private ProxyFactoryCreator proxyFactoryCreator;
    
    private String beanName;
    
    public void afterPropertiesSet() throws Exception {
        this.init();
    }
    
    @Override
    public void init() {
        if (this.proxyFactoryCreator == null) {
            this.proxyFactoryCreator = new DefaultProxyFactoryCreator();
        }
        super.init();
    }

    protected Object maybeGetProxy(ServiceRegistryEntry reference) {
        final StaticServiceReferenceProxyFactorySource proxyFactorySource = new StaticServiceReferenceProxyFactorySource(getProxyTypes(), reference);
        final ProxyFactory proxyFactory = this.proxyFactoryCreator.createProxyFactory(proxyFactorySource, beanName, null);
        return proxyFactory.getProxy();
    }

    /* ******************** ServiceProxyFactoryCreatorAware implementation ******************** */
    
    public void setProxyFactoryCreator(ProxyFactoryCreator serviceProxyFactoryCreator) {
        if (this.proxyFactoryCreator == null) {
            this.proxyFactoryCreator = serviceProxyFactoryCreator;
        }
    }
    
    /* ******************** BeanNameAware implementation ******************** */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    
    /* ******************** Injection setters ******************** */

    /**
     * Sets the proxy types for the {@link ServiceRegistrySet}. Simply delegates call
     * to superclass's {@link #setProxyTypes(Class[])} method. Allows both 
     * supportedTypes and proxyTypes to be used in populating this bean
     */
    public void setProxyTypes(Class<?>[] proxyTypes) {
        super.setProxyTypes(proxyTypes);
    }
    
}
