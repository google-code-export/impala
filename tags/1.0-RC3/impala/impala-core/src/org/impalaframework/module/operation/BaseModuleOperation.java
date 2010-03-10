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

import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.spi.TransitionManager;

/**
 * Shared base class of {@link ModuleOperation} implementation. Simply exposes getter and setter
 * for {@link ModificationExtractorRegistry}.
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleOperation extends LockingModuleOperation {

    private ModificationExtractorRegistry modificationExtractorRegistry;
    
    private TransitionManager transitionManager;
    
    protected BaseModuleOperation() {
        super();
    }

    protected ModificationExtractorRegistry getModificationExtractorRegistry() {
        return modificationExtractorRegistry;
    }

    protected TransitionManager getTransitionManager() {
        return transitionManager;
    }
    
    /* **************** Wired in setters **************** */

    public void setModificationExtractorRegistry(ModificationExtractorRegistry modificationExtractorRegistry) {
        this.modificationExtractorRegistry = modificationExtractorRegistry;
    }

    public void setTransitionManager(TransitionManager transitionManager) {
        this.transitionManager = transitionManager;
    }
    
    

}
