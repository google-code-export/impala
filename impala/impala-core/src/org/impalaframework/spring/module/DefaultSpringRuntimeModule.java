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

import org.impalaframework.module.ModuleDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class DefaultSpringRuntimeModule implements SpringRuntimeModule {

    private final ModuleDefinition moduleDefinition;
    private final ConfigurableApplicationContext applicationContext;

    public DefaultSpringRuntimeModule(ModuleDefinition moduleDefinition,
            ConfigurableApplicationContext applicationContext) {
        super();
        
        Assert.notNull(moduleDefinition);
        Assert.notNull(applicationContext);

        this.moduleDefinition = moduleDefinition;
        this.applicationContext = applicationContext;
    }
    
    public Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public ClassLoader getClassLoader() {
        return applicationContext.getClassLoader();
    }

    public ModuleDefinition getModuleDefinition() {
        return moduleDefinition;
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
