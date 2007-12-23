package org.impalaframework.web;

import org.springframework.web.context.WebApplicationContext;

public interface WebConstants {

	String PLUGIN_NAMES_PARAM = "moduleNames";
	
	String IMPALA_FACTORY_ATTRIBUTE = WebApplicationContext.class.getName() + ".FACTORY_HOLDER";

	String MODULE_DEFINITION_SOURCE_ATTRIBUTE = WebApplicationContext.class.getName() + ".MODULE_DEFINITION_SOURCE";
	
	String WEBAPP_LOCATION_PARAM = "webappConfigLocation";
	
	String ROOT_WEB_MODULE_PARAM = "rootWebModule";

	String BOOTSTRAP_LOCATIONS_RESOURCE_PARAM = "bootstrapLocationsResource";

	String BOOTSTRAP_LOCATIONS_PROPERTY_PARAM = "bootstrapLocations";
	
	String BOOTSTRAP_MODULES_RESOURCE_PARAM = "bootstrapModulesResource";

	String PARENT_LOCATIONS = "parentLocations";
	
	String MODULE_LOCATIONS_RESOURCE_PARAM = "moduleLocationsResource";
	
	/** Default config location for the root context */
	String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
	
	/** Default prefix for building a config location for a namespace */
	String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
	
	/** Default suffix for building a config location for a namespace */
	String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

	String CONTEXT_LOADER_CLASS_NAME = "contextLoaderClassName";

}

