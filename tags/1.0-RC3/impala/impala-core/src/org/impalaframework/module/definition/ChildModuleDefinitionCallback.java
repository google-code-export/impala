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
 * Represents contract for handling {@link ModuleDefinition} specific operation
 * with {@link ModuleDefinitionWalker} methods. If
 * {@link #matches(ModuleDefinition)}, this interrupts the walking process,
 * resulting in {@link ModuleDefinitionWalker} returning the matching
 * {@link ModuleDefinition}. This allows {@link ModuleDefinitionWalker} to be
 * used for searching.
 * 
 * @author Phil Zoio
 */
public interface ChildModuleDefinitionCallback extends ModuleDefinitionCallback{

    void beforeChild(ModuleDefinition moduleDefinition);

    void afterChild(ModuleDefinition moduleDefinition);

}
