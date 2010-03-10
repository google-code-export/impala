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

package org.impalaframework.spring.service;

import org.springframework.aop.framework.ProxyFactory;

/**
 * Abstraction for setting up a proxy factory, which involves setting up a {@link ServiceEndpointTargetSource}
 * as well as part-initialising the {@link ProxyFactory}.
 * 
 * Implementations are typically not responsible, however, for adding advisors or interceptors to the proxy. 
 * 
 * @author Phil Zoio
 */
public interface ProxyFactorySource {

    /**
     * Creates proxy factory.
     */
    void init();

    /**
     * Returns created {@link ProxyFactory}
     */
    ProxyFactory getProxyFactory();
    
    /**
     * Returns created {@link ServiceEndpointTargetSource}
     */
    ServiceEndpointTargetSource getTargetSource();

}
