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

import org.springframework.aop.framework.ProxyFactory;

public class ProxyFactorySourceUtils {

    protected static void addInterfaces(ProxyFactory proxyFactory, Class<?>[] interfaces) {
        
        for (int i = 0; i < interfaces.length; i++) {
            if (DynamicBeanRetrievingProxyFactorySource.logger.isDebugEnabled()) {
                DynamicBeanRetrievingProxyFactorySource.logger.debug("Adding interface " + interfaces[i] + " loaded from " + interfaces[i].getClassLoader());
            }
            proxyFactory.addInterface(interfaces[i]);
        }
    }

}
