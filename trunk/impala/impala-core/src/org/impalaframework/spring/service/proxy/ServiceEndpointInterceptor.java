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

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.Assert;

/**
 * Interceptor which uses a {@link ServiceEndpointTargetSource} to retrieve a {@link ServiceRegistryEntry}, from which it 
 * obtains a service object to which to pass to the standard Spring AOP {@link MethodInvocation#proceed()} call.
 * 
 * It also performs a number of other responsibilities, such as:
 * <ul>
 * <li>retrying for certain number of times using a certain interval if no reference could be obtained
 * <li>setting the thread context class loader to that of the class loader responsible for loading the invocation target
 * <li>logging if no service can be found
 * <li>throwing a {@link NoServiceException} if no service can be found
 * <li>or proceeding with a no-op proxy implementation of the service if no service can be found
 * </ul>
 * All of these behaviours can be enabled, disabled or controlled via configuration, as described in {@link DefaultProxyFactoryCreator}.
 * 
 * @see DefaultProxyFactoryCreator
 * 
 * @author Phil Zoio
 */
public class ServiceEndpointInterceptor implements MethodInterceptor {

    final Log log = LogFactory.getLog(ServiceEndpointInterceptor.class);
    
    /**
     * Property which applies for creation of proxies.
     * True if the interceptor should allow the call to proceed (with a dummy
     * value returned) if no service is present. Primarily present for testing
     * purposes. Defaults to false.
     */
    String PROXY_ALLOW_NO_SERVICE = "proxy.allow.no.service";

    /**
     * Property which applies for creation of proxies.
     * Whether to set the context class loader to the class loader of the module
     * contributing the bean being invoked. This is to mitigate possibility of
     * exceptions being caused by calls to
     * <code>Thread.setContextClassLoader()</code> being propagated across
     * modules
     */
    String PROXY_SET_CONTEXT_CLASSLOADER = "proxy.set.context.classloader";

    /**
     * Property which applies for creation of proxies.
     * The number of times the interceptor should retry before giving up
     * attempting to obtain a proxy. For example, if retryCount is set to three,
     * Impala will try one initially, plus another three times, before giving up
     * attempting to obtain the service reference.
     */
    String PROXY_MISSING_SERVICE_RETRY_COUNT = "proxy.missing.service.retry.count";

    /**
     * Property which applies for creation of proxies.
     * The amount of time in milliseconds between successive retries if
     * {@link #retryCount} is greater than zero
     */
    String PROXY_MISSING_SERVICE_RETRY_INTERVAL = "proxy.missing.service.retry.interval";

    private ServiceEndpointTargetSource targetSource;

    private String beanName;
    
    private ServiceEndpointOptionsHelper optionsHelper;

    public ServiceEndpointInterceptor(ServiceEndpointTargetSource targetSource, String beanName, ServiceEndpointOptionsHelper optionsHelper) {
        Assert.notNull(optionsHelper, "optionsHelper cannot be null");
        this.targetSource = targetSource;
        this.beanName = beanName;
        this.optionsHelper = optionsHelper;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        
        final Method method = invocation.getMethod();
        final Object[] arguments = invocation.getArguments();
        
        final boolean setCCCL = optionsHelper.isSetContextClassLoader();

        ServiceRegistryEntry serviceReference = targetSource.getServiceRegistryReference();
        
        int retriesUsed = 0;
        while (serviceReference == null && retriesUsed < optionsHelper.getRetryCount()) {
            try {
                Thread.sleep(optionsHelper.getRetryInterval());
            }
            catch (InterruptedException e) {
            }
            serviceReference = targetSource.getServiceRegistryReference();
            retriesUsed++;
        }

        if (serviceReference != null) {
            
            final Thread currentThread;
            final ClassLoader existingClassLoader;
            if (setCCCL) {
                currentThread = Thread.currentThread();  
                existingClassLoader= currentThread.getContextClassLoader();
            } else {
                currentThread = null;
                existingClassLoader = null;
            }
            
            try {
                if (setCCCL) {
                    currentThread.setContextClassLoader(serviceReference.getBeanClassLoader());
                }
                
                return AopUtils.invokeJoinpointUsingReflection(serviceReference.getServiceBeanReference().getService(), method, arguments);
                
            } finally {
                //reset the previous class loader
                if (setCCCL) {
                    Thread.currentThread().setContextClassLoader(existingClassLoader);
                }
            }
        }
        else {
            
            if (optionsHelper.isLogWarningNoService()) {
                log.warn("************************************************************************* ");
                log.warn("No service available for bean " + beanName + ". Proceeding with stub implementation");
                log.warn("************************************************************************* ");
            }
            
            if (optionsHelper.isProceedWithNoService()) {
                return invokeDummy(invocation);
            }
            else {
                throw new NoServiceException("No service available for bean " + beanName);
            }
        }
    }

    public Object invokeDummy(MethodInvocation invocation) throws Throwable {

        log.debug("Calling method " +  invocation);
        Class<?> returnType = invocation.getMethod().getReturnType();

        if (Void.TYPE.equals(returnType))
            return null;
        if (Byte.TYPE.equals(returnType))
            return (byte) 0;
        if (Short.TYPE.equals(returnType))
            return (short) 0;
        if (Integer.TYPE.equals(returnType))
            return (int) 0;
        if (Long.TYPE.equals(returnType))
            return 0L;
        if (Float.TYPE.equals(returnType))
            return 0f;
        if (Double.TYPE.equals(returnType))
            return 0d;
        if (Boolean.TYPE.equals(returnType))
            return false;

        return null;
    }

}
