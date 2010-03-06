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

package org.impalaframework.spring.service.proxy;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.spring.service.ProxyFactoryCreator;
import org.impalaframework.spring.service.ProxyFactorySource;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

/**
 * Implementation of {@link ProxyFactoryCreator} which is used to create proxy for service 
 * obtained from the Impala service registry.
 * 
 * @author Phil Zoio
 */
public class DefaultProxyFactoryCreator implements ProxyFactoryCreator {
    
    private static final Log logger = LogFactory.getLog(DefaultProxyFactoryCreator.class);
    
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
     * Whether to log a warning of no service is found. This will
     * typically be set to true if {@link #allowNoService} is set to true
     */
    private boolean logWarningNoService;

    /**
     * The number of times the interceptor should retry before giving up
     * attempting to obtain a proxy. For example, if retryCount is set to three,
     * Impala will try one initially, plus another three times, before giving up
     * attempting to obtain the service reference.
     */
    private int retryCount;

    /**
     * The amount of time in milliseconds between successive retries if
     * {@link #retryCount} is greater than zero
     */
    private int retryInterval;
    
    /**
     * Creates proxy factory backed by a dynamically obtained service, where the lookup assumes that the service was exported
     * using a name key entry.
     * @param proxyFactorySource the {@link ProxyFactorySource} used to create the {@link ProxyFactory} and set up the 
     * {@link ServiceEndpointTargetSource}.
     * @param the bean for which the proxy is being created.
     */
    public final ProxyFactory createProxyFactory(ProxyFactorySource proxyFactorySource, String beanName, Map<String, String> options) {
        
        proxyFactorySource.init();
        ProxyFactory proxyFactory = proxyFactorySource.getProxyFactory();
        ServiceEndpointTargetSource targetSource = proxyFactorySource.getTargetSource();
        
        addInterceptor(beanName, proxyFactory, targetSource, options);
        
        return proxyFactory;
    }

    protected void addInterceptor(String beanName, 
            ProxyFactory proxyFactory,
            ServiceEndpointTargetSource targetSource, 
            Map<String, String> options) {

        
        ServiceEndpointOptionsHelper optionsHelper = new ServiceEndpointOptionsHelper(options);
        optionsHelper.setProceedWithNoService(allowNoService);
        optionsHelper.setSetContextClassLoader(setContextClassLoader);
        optionsHelper.setLogWarningNoService(logWarningNoService);
        optionsHelper.setRetryCount(retryCount);
        optionsHelper.setRetryInterval(retryInterval);
        
        ServiceEndpointInterceptor interceptor = new ServiceEndpointInterceptor(targetSource, beanName, optionsHelper);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Creating dynamic proxy for " + beanName + 
                    " with allowNoService '" + allowNoService + "' and setContextClassLoader '" + setContextClassLoader + "'");
        }
        
        final InfrastructureProxyIntroduction infrastructureIntroduction = new InfrastructureProxyIntroduction(targetSource);
        NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor(infrastructureIntroduction);
        advisor.addMethodName("getWrappedObject");
        proxyFactory.addAdvisor(advisor);
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
     * Sets to true if the interceptor should set the current threads context
     * class loader to the class loader of the module responsible for loading
     * the target bean. Defaults to true.
     * 
     * @see Thread#setContextClassLoader(ClassLoader)
     */
    public void setSetContextClassLoader(boolean setContextClassLoader) {
        this.setContextClassLoader = setContextClassLoader;
    }

    /**
     * Sets whether to log a warning if no service can be found
     */
    public void setLogWarningNoService(boolean logWarningNoService) {
        this.logWarningNoService = logWarningNoService;
    }

    /**
     * Sets the number of times Impala will try to retrieve a service reference
     * if this is not present
     */
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    /**
     * Sets the amount of time in milliseconds between successive retries if
     * {@link #retryCount} is greater than zero
     */
    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

}
