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

package org.impalaframework.constants;

/**
 * Constants used in determing module locations
 * 
 * @author Phil Zoio
 */
public interface LocationConstants {

    String MODULE_CLASS_DIR_PROPERTY = "module.class.dir";
    String MODULE_CLASS_DIR_DEFAULT = "bin,target/classes";
    
    String MODULE_RESOURCE_DIR_PROPERTY = "module.resource.dir";
    String MODULE_RESOURCE_DIR_DEFAULT = "resources,target/classes";
    
    String MODULE_TEST_DIR_PROPERTY = "module.test.dir";
    String MODULE_TEST_DIR_DEFAULT = "bin,target/test-classes";
    
    String WORKSPACE_ROOT_PROPERTY = "workspace.root";
    String WORKSPACE_ROOT_DEFAULT = "../";
    
    String APPLICATION_VERSION = "application.version";
    String BOOTSTRAP_MODULES_RESOURCE_PARAM = "bootstrapModulesResource";
    String BOOTSTRAP_LOCATIONS_PROPERTY_PARAM = "bootstrapLocations";
    String BOOTSTRAP_LOCATIONS_RESOURCE_PARAM = "bootstrapLocationsResource";

}
