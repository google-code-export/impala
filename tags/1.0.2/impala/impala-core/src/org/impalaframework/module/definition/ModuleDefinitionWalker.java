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

import java.util.Collection;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;

/**
 * Encapsulates the process of walking a module definition hierarchy
 * @author Phil Zoio
 */
public class ModuleDefinitionWalker {
    
    /**
     * Walks the root definition, beginning by calling {@link #walkModuleDefinition(ModuleDefinition, ModuleDefinitionCallback)}
     * for the root definition itself, then for each of its siblings
     * @param root a {@link RootModuleDefinition} instance
     * @param callback a {@link ModuleDefinitionCallback} instance, to handle any per-module processing
     * @return {@link ModuleDefinition}, at the first point at which {@link ModuleDefinitionCallback#matches(ModuleDefinition)}
     * returns true. If this does not happen, then null is returned. 
     */
    public static ModuleDefinition walkRootDefinition(RootModuleDefinition root, ModuleDefinitionCallback callback) {
        
        ModuleDefinition child = walkModuleDefinition(root,callback);
        
        if (child != null)  
            return child;
        
        List<ModuleDefinition> siblings = root.getSiblings();
        
        for (ModuleDefinition moduleDefinition : siblings) {
            child = walkModuleDefinition(moduleDefinition, callback);
            if (child != null) {
                return child;
            }
        }
        return null;        
    }
    
    public static ModuleDefinition walkModuleDefinition(ModuleDefinition moduleDefinition, ModuleDefinitionCallback callback) {

        if (callback.matches(moduleDefinition)) {
            return moduleDefinition;
        }

        final Collection<ModuleDefinition> childDefinitions = moduleDefinition.getChildModuleDefinitions();
        for (ModuleDefinition childDefinition : childDefinitions) {
            
            if (callback instanceof ChildModuleDefinitionCallback) {
                ChildModuleDefinitionCallback childCallBack = (ChildModuleDefinitionCallback) callback;
                childCallBack.beforeChild(moduleDefinition);
            }
            
            final ModuleDefinition found = walkModuleDefinition(childDefinition, callback);
            if (found != null) {
                return found;
            }
            
            if (callback instanceof ChildModuleDefinitionCallback) {
                ChildModuleDefinitionCallback childCallBack = (ChildModuleDefinitionCallback) callback;
                childCallBack.afterChild(moduleDefinition);
            }
        }
        return null;
    }
    
}
