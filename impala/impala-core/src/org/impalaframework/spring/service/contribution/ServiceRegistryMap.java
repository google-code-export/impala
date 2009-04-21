package org.impalaframework.spring.service.contribution;

import java.util.Map;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.contribution.BaseServiceRegistryMap;
import org.impalaframework.spring.service.proxy.DefaultServiceProxyFactoryCreator;
import org.impalaframework.spring.service.proxy.ServiceProxyFactoryCreator;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Spring-based service registry {@link Map} implementation which uses a possibly wired in 
 * {@link ServiceProxyFactoryCreator} to create a proxy for the backed object.
 * 
 * @author Phil Zoio
 */
public class ServiceRegistryMap extends BaseServiceRegistryMap
        implements InitializingBean {

    private ServiceProxyFactoryCreator proxyFactoryCreator;

    public void afterPropertiesSet() throws Exception {
        this.init();
    }
    
    @Override
    public void init() {
        //Assert.notNull(proxyInterfaces, "proxyInterfaces cannot be null");
        //Assert.notEmpty(proxyInterfaces, "proxyInterfaces cannot be empty");
        
        if (this.proxyFactoryCreator == null) {
            this.proxyFactoryCreator = new DefaultServiceProxyFactoryCreator();
            this.proxyFactoryCreator.setServiceRegistry(this.getServiceRegistry());
        }
        
        super.init();
    }

    protected Object maybeGetProxy(ServiceRegistryReference reference) {
        final ProxyFactory proxyFactory = this.proxyFactoryCreator.createStaticProxyFactory(getSupportedTypes(), reference);
        return proxyFactory.getProxy();
    }

}
