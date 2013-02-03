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

import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.springframework.util.Assert;

/**
 * Base implementation of {@link ModuleOperation} which delegates to template method
 * {@link #doExecute(Application, ModuleOperationInput)}. {@link ModuleOperation#execute(Application, ModuleOperationInput)}
 * is final, wrapping the operation in a call to {@link ModuleStateHolder#writeLock()}, followed by a 
 * call to {@link ModuleStateHolder#writeUnlock()} in a finally block.
 * 
 * This ensure that module state changes initiated via any subclass of {@link LockingModuleOperation}
 * is synchronized.
 * 
 * @author Phil Zoio
 */
public abstract class LockingModuleOperation implements ModuleOperation {
    
    private FrameworkLockHolder frameworkLockHolder;
    
    private boolean enforceReloadability;

    /**
     * If true then allows module operation to perform
     */
    protected boolean isPermitted(ModuleOperationInput moduleOperationInput) {
    	//TODO 374 implement
		return true;
	}
    
    public ModuleOperationResult execute(
            Application application, ModuleOperationInput moduleOperationInput) {
       
        Assert.notNull(frameworkLockHolder);
        
        ModuleOperationResult execute = null;
        try {
            frameworkLockHolder.writeLock();
            
            boolean permitted = isPermitted(moduleOperationInput);
            if (permitted) {
            	execute = doExecute(application, moduleOperationInput);
            } else {
            	//374 - TODO throw exception if cannot perform
            }
        } finally {
            frameworkLockHolder.writeUnlock();
        }
        return execute;
    }

	protected abstract ModuleOperationResult doExecute(
            Application application, ModuleOperationInput moduleOperationInput);

    public void setFrameworkLockHolder(FrameworkLockHolder frameworkLockHolder) {
        this.frameworkLockHolder = frameworkLockHolder;
    }
    
    public void setEnforceReloadability(boolean enforceReloadability) {
		this.enforceReloadability = enforceReloadability;
	}

}
