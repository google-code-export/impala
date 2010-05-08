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

package org.impalaframework.service;

import org.springframework.util.Assert;

/**
 * Represents a service bean instance which is static, that is, will not change over the lifetime
 * of the underlying {@link ServiceRegistryEntry}'s existence in the {@link ServiceRegistry}.
 * 
 * @author Phil Zoio
 */
public class StaticServiceBeanReference implements ServiceBeanReference {

    private final Object service;    
    
    public StaticServiceBeanReference(Object service) {
        super();
        Assert.notNull(service);
        this.service = service;
    }

    public Object getService() {
        return service;
    }
    
    public boolean isStatic() {
        return true;
    }
    
}
