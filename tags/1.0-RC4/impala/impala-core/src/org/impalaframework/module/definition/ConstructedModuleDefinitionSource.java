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
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;

/**
 * An implementation of {@link ModuleDefinition} in which the {@link RootModuleDefinition} to be 
 * returned is supplied in the constructor.
 * @author Phil Zoio
 */
public class ConstructedModuleDefinitionSource implements ModuleDefinitionSource {

    private RootModuleDefinition definition;
    
    public ConstructedModuleDefinitionSource(RootModuleDefinition definition) {
        super();
        this.definition = definition;
    }

    public RootModuleDefinition getModuleDefinition() {
        return definition;
    }

}
