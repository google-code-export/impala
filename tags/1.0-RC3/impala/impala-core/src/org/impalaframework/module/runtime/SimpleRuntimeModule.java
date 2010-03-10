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

package org.impalaframework.module.runtime;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.springframework.util.Assert;

public class SimpleRuntimeModule implements RuntimeModule {
    
    private final ClassLoader classLoader;
    private final ModuleDefinition moduleDefinition;

    public SimpleRuntimeModule(ClassLoader classLoader,
            ModuleDefinition moduleDefinition) {
        super();
        Assert.notNull(classLoader);
        Assert.notNull(moduleDefinition);
    
        this.classLoader = classLoader;
        this.moduleDefinition = moduleDefinition;
    }

    public Object getBean(String beanName) {
        return null;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public ModuleDefinition getModuleDefinition() {
        return moduleDefinition;
    }

}
