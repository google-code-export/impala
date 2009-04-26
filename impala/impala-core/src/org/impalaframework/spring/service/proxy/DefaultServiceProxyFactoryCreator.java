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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Implementation of {@link ServiceProxyFactoryCreator} which is used to create proxy for service 
 * obtained from the Impala service registry.
 * 
 * @author Phil Zoio
 */
public class DefaultServiceProxyFactoryCreator implements ServiceProxyFactoryCreator {
    
    private static final Log logger = LogFactory.getLog(DefaultServiceProxyFactoryCreator.class);
    
    /**
     * True if the interceptor should allow the call to proceed (with a dummy
     * value returned) if no service is present. Primarily present for testing
     * purposes. Defaults to false.
     */
    private boolean allowNoService = false;

    /**
     * Whether to set the context class loader to the class loader of the module
     * contributing the bean being invoked. This is to mitigate possibility of
     * exceptions being caused by calls to
     * <code>Thread.setContextClassLoader()</code> being propagated across
     * modules
     */
    private boolean setContextClassLoader = true;
    
    /**
     * Creates proxy factory backed by a dynamically obtained service, where the lookup assumes that the service was exported
     * using a name key entry.
     * @param proxyFactorySource the {@link ProxyFactorySource} used to create the {@link ProxyFactory} and set up the 
     * {@link ContributionEndpointTargetSource}.
     * @param the bean for which the proxy is being created.
     */
    public final ProxyFactory createProxyFactory(ProxyFactorySource proxyFactorySource, String beanName) {
        
        proxyFactorySource.init();
        ProxyFactory proxyFactory = proxyFactorySource.getProxyFactory();
        ContributionEndpointTargetSource targetSource = proxyFactorySource.getTargetSource();
        
        addInterceptor(beanName, proxyFactory, targetSource);
        
        return proxyFactory;
    }

    protected void addInterceptor(String beanName, 
            ProxyFactory proxyFactory,
            ContributionEndpointTargetSource targetSource) {
        
        ContributionEndpointInterceptor interceptor = new ContributionEndpointInterceptor(targetSource, beanName);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Creating dynamic proxy for " + beanName + 
                    " with allowNoService '" + allowNoService + "' and setContextClassLoader '" + setContextClassLoader + "'");
        }
        
        interceptor.setProceedWithNoService(allowNoService);
        interceptor.setSetContextClassLoader(setContextClassLoader);
        proxyFactory.addAdvice(interceptor);
    }

    /**
     * Sets to true if the interceptor should allow the call to proceed (with a dummy value returned) if no service is
     * present. Primarily present for testing purposes. Defaults to false.
     */
    public void setAllowNoService(boolean allowNoService) {
        this.allowNoService = allowNoService;
    }
    
    /**
     * Sets to true if the interceptor should set the current threads context class loader to the class loader of the module
     * responsible for loading the target bean. Defaults to true.
     * 
     * @see Thread#setContextClassLoader(ClassLoader)
     */
    public void setSetContextClassLoader(boolean setContextClassLoader) {
        this.setContextClassLoader = setContextClassLoader;
    }
    
}
