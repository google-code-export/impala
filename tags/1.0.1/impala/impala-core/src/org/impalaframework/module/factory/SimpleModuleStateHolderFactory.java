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

import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.ModuleStateHolderFactory;

/**
 * Creates a {@link ModuleStateHolder} instance. The method {@link #newDefaultModuleStateHolder()}
 * returns an instance of {@link DefaultModuleStateHolder}, which has the {@link #externalRootModuleName}
 * injected into it.
 * 
 * @author Phil Zoio
 */
public class SimpleModuleStateHolderFactory implements ModuleStateHolderFactory {
    
    private String externalRootModuleName;
    
    public ModuleStateHolder newModuleStateHolder() {
        DefaultModuleStateHolder defaultModuleStateHolder = newDefaultModuleStateHolder();
        defaultModuleStateHolder.setExternalRootModuleName(externalRootModuleName);
        return defaultModuleStateHolder;
    }

    protected DefaultModuleStateHolder newDefaultModuleStateHolder() {
        return new DefaultModuleStateHolder();
    }
    
    public void setExternalRootModuleName(String externalRootModuleName) {
        this.externalRootModuleName = externalRootModuleName;
    }
    
}
