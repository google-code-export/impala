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

import java.util.Map;

import org.impalaframework.module.ModuleDefinition;

/**
 * @author Phil Zoio
 */
public class SimpleModuleDefinition extends BaseModuleDefinition {
    
    private static final long serialVersionUID = 1L;

    /* ********************* constructors ******************** */

    public SimpleModuleDefinition(String name) {
        this(null, name, null);
    }

    public SimpleModuleDefinition(ModuleDefinition parent, String name) {
        this(parent, name, null);
    }

    public SimpleModuleDefinition(ModuleDefinition parent, String name, String[] configLocations) {
        this(parent, name, ModuleTypes.APPLICATION, configLocations, null, null, null);
    }
    
    public SimpleModuleDefinition(
            ModuleDefinition parent, 
            String name, 
            String type, 
            String[] configLocations, 
            String[] dependencies, 
            Map<String, String> attributes, 
            String runtime) {
        super(parent, name, type, dependencies, configLocations, attributes, runtime);
    }

    /* ********************* read-only methods ******************** */

    public ModuleDefinition findChildDefinition(String moduleName, boolean exactMatch) {
        return ModuleDefinitionWalker.walkModuleDefinition(this, new ModuleMatchingCallback(moduleName, exactMatch));
    }
    
}
