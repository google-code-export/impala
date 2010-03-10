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

/**
 * Represents the service instance contributed to the Impala {@link ServiceRegistry}.
 * Can back a static service as well as a service retrieved dynamically.
 * @author Phil Zoio
 */
public interface ServiceBeanReference {

    /**
     * Returns the bean instance, typically obtained statically for singletons or dynamically
     * (via a lookup against the appropriate bean factory) for non-singletons. 
     */
    public Object getService();
 
    /**
     * Whether the reference is static. If not, then the service instance cannot for example be used
     * in {@link org.impalaframework.service.contribution.BaseServiceRegistryList} and {@link org.impalaframework.service.contribution.BaseServiceRegistryMap}
     * subclasses.
     */
    public boolean isStatic();
}
