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

import java.util.List;

/**
 * Extension of {@link ModuleDefinition} with methods specific to the "root"
 * module. Note that only one module can be considered the root module. However,
 * not all modules need to have the root module as a direct ancestors. Modules
 * which fall into this group are treated as "siblings" of the root module, and
 * can be retrieved using {@link RootModuleDefinition#getSiblings()} and
 * {@link RootModuleDefinition#hasSibling(String)}.
 * 
 * @author Phil Zoio
 */
public interface RootModuleDefinition extends ModuleDefinition {

    /**
     * Returns sibling modules, that is modules for which the current
     * {@link RootModuleDefinition} instance is not an ancestor.
     */
    List<ModuleDefinition> getSiblings();

    /**
     * Returns true if this {@link RootModuleDefinition} instance has a sibling
     * module with the specified name.
     */
    boolean hasSibling(String name);

    /**
     * Returns the named sibling module if present, otherwise returns null.
     */
    ModuleDefinition getSiblingModule(String name);

    /**
     * Adds a sibling to the root module definition
     */
    void addSibling(ModuleDefinition siblingDefinition);

}
