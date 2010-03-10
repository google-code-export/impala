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

package org.impalaframework.spring.module.loader;

import java.util.Map;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.registry.Registry;
import org.impalaframework.registry.RegistrySupport;
import org.impalaframework.spring.module.DelegatingContextLoader;

/**
 * Holds a mapping of module types to {@link DelegatingContextLoader} instances, where module types are determined using the
 * call {@link ModuleDefinition#getType()}.
 *  
 * @author Phil Zoio
 */
public class DelegatingContextLoaderRegistry extends RegistrySupport implements Registry<DelegatingContextLoader> {
    
    public void addItem(String type, DelegatingContextLoader moduleLoader) {
        super.addRegistryItem(type, moduleLoader);
    }

    public DelegatingContextLoader getDelegatingLoader(String type) {
        return super.getEntry(type, DelegatingContextLoader.class, false);
    }

    public boolean hasDelegatingLoader(String type) {
        return (super.getEntry(type, DelegatingContextLoader.class, false) != null);
    }

    public void setDelegatingLoaders(Map<String, DelegatingContextLoader> delegatingLoaders) {
        super.setEntries(delegatingLoaders);
    }
    
}
