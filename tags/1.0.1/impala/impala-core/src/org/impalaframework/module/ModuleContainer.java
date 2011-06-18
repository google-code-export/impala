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

package org.impalaframework.module;

import java.io.Serializable;
import java.util.Collection;

/**
 * Defines methods for a module container, that is an object which contains
 * other {@link ModuleDefinition} instances.
 * 
 * @author Phil Zoio
 */
public interface ModuleContainer extends Serializable {

    /**
     * Returns the names of the child modules managed by this {@link ModuleContainer} instance.
     */
    Collection<String> getChildModuleNames();

    /**
     * Returns the {@link ModuleDefinition} of a named child module. Only returns non null if module container directly manages a module of this name.
     */
    ModuleDefinition getChildModuleDefinition(String moduleName);

    /**
     * Returns true if module container directly manages a module of this name.
     */
    boolean hasChildModuleDefinition(String moduleName);
    
    /**
     * Returns the definitions of the child modules managed by this {@link ModuleContainer} instance.
     */
    Collection<ModuleDefinition> getChildModuleDefinitions();

    /**
     * Adds a definition to be contained by this {@link ModuleContainer} instance.
     */
    void addChildModuleDefinition(ModuleDefinition moduleDefinition);

    /**
     * Removes a definition from being contained by this {@link ModuleContainer} instance.
     */
    ModuleDefinition removeChildModuleDefinition(String moduleName);

}
