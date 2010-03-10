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

package org.impalaframework.module.operation;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TransitionResultSet;
import org.springframework.util.Assert;

/**
 * {@link ModuleOperation} implementation which contains functionality for implementing reloading of a named module.
 * Unlike {@link ReloadNamedModuleOperation}, this class does not require an exact match of the module to be renamed.
 * Instead, it searches the module hierarchy/graph, and reloads the first module whose name contains the supplied
 * String on which to match.
 * 
 * @see ReloadNamedModuleOperation
 * @author Phil Zoio
 */
public class ReloadModuleNamedLikeOperation extends BaseModuleOperation {
    
    private ModuleOperationRegistry moduleOperationRegistry;

    protected ReloadModuleNamedLikeOperation() {
        super();
    }

    public ModuleOperationResult doExecute(Application application, ModuleOperationInput moduleOperationInput) {

        Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
        String moduleToReload = moduleOperationInput.getModuleName();
        Assert.notNull(moduleToReload,
                "moduleName is required as it specifies the name used to match the module to reload in "
                        + this.getClass().getName());
        
        ModuleStateHolder moduleStateHolder = application.getModuleStateHolder();
        RootModuleDefinition newDefinition = moduleStateHolder.cloneRootModuleDefinition();

        ModuleDefinition found = newDefinition.findChildDefinition(moduleToReload, false);

        if (found != null) {

            String foundModuleName = found.getName();
            
            Assert.notNull(moduleOperationRegistry, "moduleOperationRegistry cannot be null");
            
            ModuleOperation operation = moduleOperationRegistry.getOperation(
                    ModuleOperationConstants.ReloadNamedModuleOperation);
            ModuleOperationResult execute = operation.execute(application, new ModuleOperationInput(null, null, foundModuleName));
            TransitionResultSet transitionResultSet = execute.getTransitionResultSet();

            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("moduleName", foundModuleName);
            return new ModuleOperationResult(transitionResultSet, resultMap);
        }
        else {
            return ModuleOperationResult.EMPTY;
        }
    }

    public void setModuleOperationRegistry(ModuleOperationRegistry moduleOperationRegistry) {
        this.moduleOperationRegistry = moduleOperationRegistry;
    }
}
