package org.impalaframework.spring.service.contribution;

import java.util.Map;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.contribution.BaseServiceRegistryMap;
import org.impalaframework.spring.service.proxy.DefaultServiceProxyFactoryCreator;
import org.impalaframework.spring.service.proxy.ServiceProxyFactoryCreator;
import org.impalaframework.spring.service.proxy.StaticServiceReferenceProxyFactorySource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * Spring-based service registry {@link Map} implementation which uses a possibly wired in 
 * {@link ServiceProxyFactoryCreator} to create a proxy for the backed object.
 * 
 * @author Phil Zoio
 */
public class ServiceRegistryMap extends BaseServiceRegistryMap
        implements InitializingBean, BeanNameAware {

    private ServiceProxyFactoryCreator proxyFactoryCreator;
    
    private String beanName;
    
    public void afterPropertiesSet() throws Exception {
        this.init();
    }
    
    @Override
    public void init() {
        if (this.proxyFactoryCreator == null) {
            this.proxyFactoryCreator = new DefaultServiceProxyFactoryCreator();
        }
        
        super.init();
    }

    protected Object maybeGetProxy(ServiceRegistryReference reference) {
        final StaticServiceReferenceProxyFactorySource proxyFactorySource = new StaticServiceReferenceProxyFactorySource(getSupportedTypes(), reference);
        final ProxyFactory proxyFactory = this.proxyFactoryCreator.createProxyFactory(proxyFactorySource, beanName);
        return proxyFactory.getProxy();
    }

    /* ******************** BeanNameAware implementation ******************** */
    
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    
    /* ******************** Injection setters ******************** */

    /**
     * Sets the proxy types for the {@link ServiceRegistryMap}. Simply delegates call
     * to superclass's {@link #setSupportedTypes(Class[])} method. Allows both 
     * supportedTypes and proxyTypes to be used in populating this bean
     */
    public void setProxyTypes(Class<?>[] proxyTypes) {
        super.setSupportedTypes(proxyTypes);
    }
    
}
