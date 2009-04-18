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

package org.impalaframework.service.contribution;

import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.proxy.ProxyHelper;

/**
 * Adds to functionality of {@link BaseServiceRegistryMap} by providing an implementation of 
 * {@link #maybeGetProxy(ServiceRegistryReference)} which uses {@link ProxyHelper}
 * 
 * @author Phil Zoio
 */
public class ServiceRegistryMap extends BaseServiceRegistryMap {
    
    private ProxyHelper proxyHelper;
    
    public ServiceRegistryMap() {
        super();
        proxyHelper = new ProxyHelper();
    }
    
    protected Object maybeGetProxy(ServiceRegistryReference reference) {
        return proxyHelper.maybeGetProxy(reference);
    }

    public void setProxyEntries(boolean proxyEntries) {
        this.proxyHelper.setProxyEntries(proxyEntries);
    }
    

    public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
        this.proxyHelper.setProxyInterfaces(proxyInterfaces);
    }
}
