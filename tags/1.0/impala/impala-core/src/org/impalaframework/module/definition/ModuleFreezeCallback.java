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

package org.impalaframework.module.definition;

import org.impalaframework.module.ModuleDefinition;

/**
 * {@link ModuleDefinitionCallback} implementation which calls {@link ModuleDefinition#freeze()}
 * on each module definition passed to {@link #matches(ModuleDefinition)}.
 * @author Phil Zoio
 */
public class ModuleFreezeCallback implements ModuleDefinitionCallback {
    
    private boolean freeze;
    
    public ModuleFreezeCallback(boolean freeze) {
        super();
        this.freeze = freeze;
    }
    
    public boolean matches(ModuleDefinition moduleDefinition) {
        if (freeze) moduleDefinition.freeze();
        else moduleDefinition.unfreeze();
        return false;
    }

}
