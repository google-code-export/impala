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

package org.impalaframework.module.metadata;

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.spring.module.application.ApplicationAware;
import org.springframework.util.Assert;

/**
 * Simple facade useful for determining whether a particular module is present
 * @author Phil Zoio
 */
public class ModuleMetadataHelper implements ApplicationAware {

    private ModuleStateHolder moduleStateHolder;
    
    public boolean isModulePresent(String moduleName) {
        Assert.notNull(moduleName, "moduleName cannot be null");
        Assert.notNull(moduleStateHolder, "moduleStateHolder cannot be null");
        return moduleStateHolder.hasModule(moduleName);
    }
    
    public boolean isModuleDefinitionPresent(String moduleName) {
        Assert.notNull(moduleName, "moduleName cannot be null");
        Assert.notNull(moduleStateHolder, "moduleStateHolder cannot be null");
        RootModuleDefinition rootModuleDefinition = moduleStateHolder.getModuleDefinition();
        boolean isPresent = rootModuleDefinition.hasChildModuleDefinition(moduleName);
        if (!isPresent) {
            isPresent = rootModuleDefinition.getName().equals(moduleName);
        }
        return isPresent;
    }

    public void setApplication(Application application) {
        moduleStateHolder = application.getModuleStateHolder();
    }

}
