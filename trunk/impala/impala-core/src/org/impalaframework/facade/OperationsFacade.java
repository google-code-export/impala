/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.facade;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.RuntimeModule;

public interface OperationsFacade {

    void init(ModuleDefinitionSource source);

    boolean reload(String moduleName);

    String reloadLike(String likeModuleName);

    void reloadRootModule();

    void repairModules();
    
    void unloadRootModule();

    boolean remove(String moduleName);

    void addModule(final ModuleDefinition moduleDefinition);

    boolean hasModule(String moduleName);

    String findLike(String moduleName);

    RuntimeModule getRootRuntimeModule();

    RuntimeModule getRuntimeModule(String moduleName);

    <T extends Object> T getBean(String beanName, Class<T> t);

    <T extends Object> T getModuleBean(String moduleName, String beanName, Class<T> type);
    
    RootModuleDefinition getRootModuleDefinition();

}
