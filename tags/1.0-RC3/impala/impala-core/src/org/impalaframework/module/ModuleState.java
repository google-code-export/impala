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

public interface ModuleState {

    /**
     * Module has been marked for loading or is in process of being loaded
     */
    String LOADING = "LOADING";
    
    /**
     * Module is successfully loaded and active
     */
    String LOADED = "LOADED";
    
    /**
     * Module is marked for unloading or is in process of being unloaded
     */
    String UNLOADING = "UNLOADING";
    
    /**
     * Module has been successfully unloaded
     */
    String UNLOADED = "UNLOADED";
    
    /**
     * State is not clear, as module unloading failed
     */
    String UNKNOWN = "UNKNOWN";
    
    /**
     * Module marked as stale - eligible for reloading
     */
    String STALE = "STALE";
    
    /**
     * Module load unsuccessful
     */
    String ERROR = "ERROR";
    
    /**
     * Module that was marked LOADING was not loaded because loading of one of its dependencies failed.
     */
    String DEPENDENCY_FAILED = "DEPENDENCY_FAILED";

}
