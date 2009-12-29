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

package org.impalaframework.module.transition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleRuntimeManager;
import org.impalaframework.module.spi.TransitionProcessor;
import org.springframework.util.Assert;

public class UnloadTransitionProcessor implements TransitionProcessor {
    
    private static final Log logger = LogFactory.getLog(UnloadTransitionProcessor.class);

    private ModuleRuntimeManager moduleRuntimeManager;  
    
    public void process(Application application, RootModuleDefinition rootDefinition, ModuleDefinition moduleDefinition) {
        
        Assert.notNull(moduleDefinition);
        Assert.notNull(moduleRuntimeManager);
        boolean closeModule = moduleRuntimeManager.closeModule(application, moduleDefinition);
        
        if (closeModule) {
            moduleDefinition.setState(ModuleState.UNLOADED);
        } else {
            moduleDefinition.setState(ModuleState.UNKNOWN);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Marked '" + moduleDefinition.getName() + "' to state " + moduleDefinition.getState());
        }
    }

    public void setModuleRuntimeManager(ModuleRuntimeManager moduleRuntimeManager) {
        this.moduleRuntimeManager = moduleRuntimeManager;
    }
}
