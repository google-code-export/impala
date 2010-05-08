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

package org.impalaframework.module.loader;

import java.util.Map;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.registry.Registry;
import org.impalaframework.registry.RegistrySupport;
import org.springframework.util.Assert;

/**
 * Holds a mapping of {@link ModuleLoader} instances to module types, as
 * determined using the {@link ModuleDefinition#getType()} call.
 * 
 * @author Phil Zoio
 */
public class ModuleLoaderRegistry extends RegistrySupport implements Registry<ModuleLoader> {

    public ModuleLoader getModuleLoader(String key) {
        return getModuleLoader(key, true);
    }

    public ModuleLoader getModuleLoader(String key, boolean failIfNotFound) {
        return super.getEntry(key, ModuleLoader.class, failIfNotFound);
    }

    public void addItem(String key, ModuleLoader moduleLoader) {
        super.addRegistryItem(key, moduleLoader);
    }

    public boolean hasModuleLoader(String key) {
        Assert.notNull(key, "type cannot be null");
        return (getModuleLoader(key, false) != null);
    }
    
    public void setModuleLoaders(Map<String, ModuleLoader> moduleLoaders) {
        super.setEntries(moduleLoaders);
    }
    
}
