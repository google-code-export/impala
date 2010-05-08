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

package org.impalaframework.module.factory;

import org.impalaframework.module.holder.ModuleClassLoaderRegistry;
import org.impalaframework.module.spi.ClassLoaderRegistry;
import org.impalaframework.module.spi.ClassLoaderRegistryFactory;

/**
 * Implementation of {@link ClassLoaderRegistry} instance which simply returns a
 * {@link ModuleClassLoaderRegistry} instance in its
 * {@link #newClassLoaderRegistry()} implementation.
 * @author Phil Zoio
 */
public class SimpleClassLoaderRegistryFactory implements ClassLoaderRegistryFactory {

    public ClassLoaderRegistry newClassLoaderRegistry() {
        return new ModuleClassLoaderRegistry();
    }
    
}
