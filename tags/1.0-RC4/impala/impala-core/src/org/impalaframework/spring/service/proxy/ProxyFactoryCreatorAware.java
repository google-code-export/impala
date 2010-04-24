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

import org.impalaframework.spring.service.ProxyFactoryCreator;

/**
 * Interface which beans can implement to be notified of the application-wide {@link ProxyFactoryCreator}.
 * Typically, implementors will first check to see whether this property is set before populating the field
 * This is so a {@link ProxyFactoryCreator} can be set at the bean level
 * 
 * @author Phil Zoio
 */
public interface ProxyFactoryCreatorAware {
    
    public void setProxyFactoryCreator(ProxyFactoryCreator proxyFactoryCreator);
}
