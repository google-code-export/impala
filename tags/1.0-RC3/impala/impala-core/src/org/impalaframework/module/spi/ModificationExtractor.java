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

package org.impalaframework.module.spi;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;


/**
 * The job of a {@link ModificationExtractor} is to determine what changes are required
 * to convert from the currently loaded module hierarchy (which at pre-load time
 * will be empty), to a desired module hierarchy as determined by a second
 * hierarchy of {@link ModuleDefinition}s.
 * <p>
 * For example, suppose module A is the root module and it's child, module B, is
 * loaded. Suppose also that change the desired module hierarchy so that module
 * A will still be loaded, but it will have just one child, module C. The
 * actions that need to take place are: B must be unloaded C must be loaded
 * <p>
 * The advantage of the use of the {@link ModificationExtractor} is that it is possible
 * to edit a module configuration hierarchy offline, then apply it in one go,
 * without having to work out individually what are the module changes required
 * to effect this change.
 * 
 * @author Phil Zoio
 */
public interface ModificationExtractor {

    /**
     * Returns the transitions required to convert from one module hierarchy to another
     */
    TransitionSet getTransitions(Application application, RootModuleDefinition originalDefinition, RootModuleDefinition newDefinition);

}
