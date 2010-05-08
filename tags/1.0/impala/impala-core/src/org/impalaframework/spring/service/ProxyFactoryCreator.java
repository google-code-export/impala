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

import java.util.Map;

import org.springframework.aop.framework.ProxyFactory;

/**
 * Encapsulates the mechanism for creating a {@link ProxyFactory} which backs a service/bean
 * obtained from the Impala service registry.
 * 
 * @author Phil Zoio
 */
public interface ProxyFactoryCreator {

    /**
     * @param proxyFactorySource which handles the details of creating the
     * {@link ProxyFactory} and associated
     * {@link ServiceEndpointTargetSource} instance.
     * @param beanName the name of the bean on behalf of which the service proxy
     * is being created
     * @param options values which can override the default properties set for the proxy factory creator
     * @return a {@link ProxyFactory}
     */
    public ProxyFactory createProxyFactory(ProxyFactorySource proxyFactorySource, String beanName, Map<String, String> options);

}
