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
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleRuntime;

/**
 * Implementation of {@link ModuleRuntime} which encapsulates a Spring module runtime.
 * @author Phil Zoio
 */
public class SimpleModuleRuntime extends BaseModuleRuntime {

    protected RuntimeModule doLoadModule(Application application, final ClassLoader classLoader, ModuleDefinition definition) {
        return new SimpleRuntimeModule(classLoader, definition);
    }

    @Override
    protected void doCloseModule(String applicationId, RuntimeModule runtimeModule) {
    }

    public String getRuntimeName() {
        return "simple";
    }

}
