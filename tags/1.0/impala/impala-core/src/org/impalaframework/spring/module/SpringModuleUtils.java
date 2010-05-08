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

package org.impalaframework.spring.module;

import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.util.ObjectUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public abstract class SpringModuleUtils {

    public static ConfigurableApplicationContext getRootSpringContext(ModuleStateHolder moduleStateHolder) {

        Assert.notNull(moduleStateHolder);
        
        final RuntimeModule runtimeModule = moduleStateHolder.getExternalRootModule();
        return getModuleSpringContext(runtimeModule);
    }
    
    public static ConfigurableApplicationContext getModuleSpringContext(ModuleStateHolder moduleStateHolder, String moduleName) {

        Assert.notNull(moduleStateHolder);
        Assert.notNull(moduleName);
        
        final RuntimeModule runtimeModule = moduleStateHolder.getModule(moduleName);
        return getModuleSpringContext(runtimeModule);
    }


    public static ConfigurableApplicationContext getModuleSpringContext(final RuntimeModule runtimeModule) {
        if (runtimeModule == null) {
            return null;
        }
        ConfigurableApplicationContext context = ObjectUtils.cast(
                runtimeModule, SpringRuntimeModule.class)
                .getApplicationContext();
        return context;
    }
}
