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

import java.util.Collections;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;

/**
 * Interceptor which uses a {@link ServiceEndpointTargetSource} to retrieve a
 * {@link ServiceRegistryEntry}, from which it obtains a service object to which
 * to pass to the standard Spring AOP {@link MethodInvocation#proceed()} call.
 * 
 * It also performs a number of other responsibilities, such as:
 * <ul>
 * <li>retrying for certain number of times using a certain interval if no
 * reference could be obtained
 * <li>setting the thread context class loader to that of the class loader
 * responsible for loading the invocation target
 * <li>logging if no service can be found
 * <li>throwing a {@link NoServiceException} if no service can be found
 * <li>or proceeding with a no-op proxy implementation of the service if no
 * service can be found
 * </ul>
 * All of these behaviours can be enabled, disabled or controlled via
 * configuration, as described in {@link DefaultProxyFactoryCreator}.
 * 
 * @see DefaultProxyFactoryCreator
 * 
 * @author Phil Zoio
 */
public class ServiceEndpointOptionsHelper {

    /**
     * Property which applies for creation of proxies. True if the interceptor
     * should allow the call to proceed (with a dummy value returned) if no
     * service is present. Primarily present for testing purposes. Defaults to
     * false.
     */
    String PROXY_ALLOW_NO_SERVICE = "allow.no.service";
    
    /**
     * Whether to log a warning if no service is found
     */
    String PROXY_LOG_WARNING_NO_SERVICE = "log.warning.no.service";

    /**
     * Property which applies for creation of proxies. Whether to set the
     * context class loader to the class loader of the module contributing the
     * bean being invoked. This is to mitigate possibility of exceptions being
     * caused by calls to <code>Thread.setContextClassLoader()</code> being
     * propagated across modules
     */
    String PROXY_SET_CONTEXT_CLASSLOADER = "set.context.classloader";

    /**
     * Property which applies for creation of proxies. The number of times the
     * interceptor should retry before giving up attempting to obtain a 
     * For example, if retryCount is set to three, Impala will try one
     * initially, plus another three times, before giving up attempting to
     * obtain the service reference.
     */
    String PROXY_MISSING_SERVICE_RETRY_COUNT = "missing.service.retry.count";

    /**
     * Property which applies for creation of proxies. The amount of time in
     * milliseconds between successive retries if {@link #retryCount} is greater
     * than zero
     */
    String PROXY_MISSING_SERVICE_RETRY_INTERVAL = "missing.service.retry.interval";

    private final Map<String, String> options;

    @SuppressWarnings("unchecked")
    public ServiceEndpointOptionsHelper(Map<String, String> options) {
        super();
        this.options =(options != null ? Collections.unmodifiableMap(options) : Collections.EMPTY_MAP);
    }

    private boolean proceedWithNoService;

    private boolean logWarningNoService;

    private boolean setContextClassLoader;

    private int retryCount;

    private int retryInterval;

    public void setProceedWithNoService(boolean proceedWithNoService) {
        this.proceedWithNoService = getBooleanOption(PROXY_ALLOW_NO_SERVICE, proceedWithNoService);
    }

    public void setLogWarningNoService(boolean logWarningNoService) {
        this.logWarningNoService = getBooleanOption(PROXY_LOG_WARNING_NO_SERVICE, logWarningNoService);
    }

    public void setSetContextClassLoader(boolean setContextClassLoader) {
        this.setContextClassLoader = getBooleanOption(PROXY_SET_CONTEXT_CLASSLOADER, setContextClassLoader);
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = getIntOption(PROXY_MISSING_SERVICE_RETRY_COUNT, retryCount);
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = getIntOption(PROXY_MISSING_SERVICE_RETRY_INTERVAL, retryInterval);
    }

    private boolean getBooleanOption(String optionName, boolean defaultValue) {
        String option = options.get(optionName);
        return option != null ? Boolean.parseBoolean(option) : defaultValue;
    }

    private int getIntOption(String optionName, int defaultValue) {
        String option = options.get(optionName);
        Integer value = null;
        if (option != null) {
            try {
                value = Integer.valueOf(option);
            }
            catch (NumberFormatException e) {
                throw new ConfigurationException("Invalid value '" + option + "' for option '" + optionName + "'. Must be integer");
            }
        }
        return value != null ? value : defaultValue;
    }

    public boolean isProceedWithNoService() {
        return proceedWithNoService;
    }

    public boolean isLogWarningNoService() {
        return logWarningNoService;
    }

    public boolean isSetContextClassLoader() {
        return setContextClassLoader;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    

}
