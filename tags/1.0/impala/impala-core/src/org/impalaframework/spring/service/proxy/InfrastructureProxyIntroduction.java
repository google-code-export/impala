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

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.core.InfrastructureProxy;

/**
 * Mixin which implements {@link InfrastructureProxy}, used for comparison of
 * two objects which potentially share the same service but use different
 * proxies.
 * @author Phil Zoio
 */
public class InfrastructureProxyIntroduction extends DelegatingIntroductionInterceptor implements InfrastructureProxy {
    
    private static final long serialVersionUID = 1L;

    private ServiceEndpointTargetSource targetSource;
    
    public InfrastructureProxyIntroduction(ServiceEndpointTargetSource targetSource) {
        super();
        this.targetSource = targetSource;
    }

    private ServiceRegistryEntry initServiceRegistryEntry() {
        return this.targetSource.getServiceRegistryReference();
    }

    public Object getWrappedObject() {        
        try {
            Object target = this.targetSource.getTarget();
            
            while (target instanceof InfrastructureProxy) {
                target = ((InfrastructureProxy)target).getWrappedObject();
            }
            
            return target;
        }
        catch (Exception e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public int hashCode() {
        ServiceRegistryEntry entry = initServiceRegistryEntry();
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((entry == null) ? 0 : entry.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        ServiceRegistryEntry entry =    
            initServiceRegistryEntry();
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InfrastructureProxyIntroduction other = (InfrastructureProxyIntroduction) obj;
        final ServiceRegistryEntry otherEntry = other.initServiceRegistryEntry();
        if (entry == null) {
            if (otherEntry != null)
                return false;
        }
        else if (!entry.equals(otherEntry))
            return false;
        return true;
    }

    

}
