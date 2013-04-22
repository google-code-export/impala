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

/**
 * Defines strategy for letting module runtime monitor know that loading is about to commence,
 * and when it has completed. Designed for use with {@link ModuleRuntime} implementation.
 * @see ModuleRuntime
 * @author Phil Zoio
 */
public interface ModuleRuntimeMonitor {

    void beforeModuleLoads(ModuleDefinition definition);

    void afterModuleLoaded(ModuleDefinition definition);

}
