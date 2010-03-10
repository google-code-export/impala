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

import org.impalaframework.module.spi.Application;

/**
 * {@link ModuleOperation} is an abstraction which models user initiated
 * operations on modules. Examples include:
 * <ul>
 * <li>Reload a particular named module
 * <li>Reload a module with a name like X
 * <li>Remove a named module
 * <li>Add bean definitions to the root module
 * <li>Shut down the entire module hierarchy etc.
 * </ul>
 * 
 * {@link ModuleOperation} is a high level interface which can used by clients
 * to get a particular job done.
 * 
 * @author Phil Zoio
 */
public interface ModuleOperation {  
    
    /**
     * Applies the module operation on the supplied {@link Application} instance, using the parameters contained in {@link ModuleOperationInput}
     * @param application an {@link Application} instance, typically obtained using {@link org.impalaframework.module.spi.ApplicationManager#getCurrentApplication()}
     * @param moduleOperationInput contains metadata parameter information for the operation
     * @return a {@link ModuleOperationResult} instance, which encapsulates results on the operations executed
     */
    public ModuleOperationResult execute(Application application, ModuleOperationInput moduleOperationInput);
}
