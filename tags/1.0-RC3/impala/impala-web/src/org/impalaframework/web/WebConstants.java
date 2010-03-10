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

package org.impalaframework.web;

import org.impalaframework.constants.LocationConstants;
import org.springframework.web.context.WebApplicationContext;

public interface WebConstants {

    String MODULE_NAMES_PARAM = "moduleNames";
    
    String ROOT_MODULE_NAME_PARAM = "rootModuleName";
    
    String IMPALA_FACTORY_ATTRIBUTE = WebApplicationContext.class.getName() + ".FACTORY_HOLDER";

    String MODULE_DEFINITION_SOURCE_ATTRIBUTE = WebApplicationContext.class.getName() + ".MODULE_DEFINITION_SOURCE";
    
    String WEBAPP_LOCATION_PARAM = "webappConfigLocation";
    
    String ROOT_WEB_MODULE_PARAM = "rootWebModule";

    String BOOTSTRAP_LOCATIONS_RESOURCE_PARAM = LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM;

    String BOOTSTRAP_LOCATIONS_PROPERTY_PARAM = LocationConstants.BOOTSTRAP_LOCATIONS_PROPERTY_PARAM;
    
    String BOOTSTRAP_MODULES_RESOURCE_PARAM = LocationConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM;

    String PARENT_LOCATIONS = "parentLocations";
    
    String MODULE_LOCATIONS_RESOURCE_PARAM = "moduleLocationsResource";
    
    String SERVLET_MODULE_ATTRIBUTE_PREFIX = WebApplicationContext.class.getName() + ".SERVLET_MODULE.";
    
    String FILTER_MODULE_ATTRIBUTE_PREFIX = WebApplicationContext.class.getName() + ".FILTER_MODULE.";
    
    String MODULE_SERVLET_APPLICATIONCONTEXT_ATTRIBUTE = "moduleServletApplicationContext";
    
    /** Default config location for the root context */
    String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
    
    /** Default prefix for building a config location for a namespace */
    String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
    
    /** Default suffix for building a config location for a namespace */
    String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

    String CONTEXT_LOADER_CLASS_NAME = "contextLoaderClassName";
    
    String REQUEST_WRAPPER_FACTORY_BEAN_NAME = "requestWrapperFactory";
    
    String REQUEST_MODULE_MAPPER_CLASS_NAME = "requestModuleMapperClassName";

}

