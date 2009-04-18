package org.impalaframework.spring.service.contribution;

import java.util.Map;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.contribution.BaseServiceRegistryMap;
import org.impalaframework.spring.service.proxy.DynamicServiceProxyFactoryCreator;
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
    private Class<?>[] proxyInterfaces;

    public void afterPropertiesSet() throws Exception {
        
        //FIXME test
        if (this.proxyFactoryCreator == null) {
            this.proxyFactoryCreator = new DynamicServiceProxyFactoryCreator();
            this.proxyFactoryCreator.setServiceRegistry(this.getServiceRegistry());
        }
        
        //call the superclass's init method
        super.init();
    }
    
    @Override
    public void init() {
        //no op - uses the afterPropertiesSet method instead
        //FIXME move from org.impalaframework.service.contribution.ServiceRegistryMap
    }

    protected Object maybeGetProxy(ServiceRegistryReference reference) {
        final ProxyFactory proxyFactory = this.proxyFactoryCreator.createStaticProxyFactory(proxyInterfaces, reference);
        return proxyFactory.getProxy();
    }
    
    public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }

}
