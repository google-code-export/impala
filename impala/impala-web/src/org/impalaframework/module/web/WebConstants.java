package org.impalaframework.module.web;

import org.springframework.web.context.WebApplicationContext;

public interface WebConstants {

	String PLUGIN_NAMES_PARAM = "pluginNames";
	
	String IMPALA_FACTORY_ATTRIBUTE = WebApplicationContext.class.getName() + ".FACTORY_HOLDER";

	String PLUGIN_SPEC_BUILDER_ATTRIBUTE = WebApplicationContext.class.getName() + "pluginSpecBuilder";
	
	String WEBAPP_LOCATION_PARAM = "webappConfigLocation";
	
	String ROOT_WEB_PLUGIN_PARAM = "rootWebPlugin";

	String BOOTSTRAP_LOCATIONS_RESOURCE_PARAM = "bootstrapLocationsResource";

	String BOOTSTRAP_LOCATIONS_PROPERTY_PARAM = "bootstrapLocations";
	
	String BOOTSTRAP_PLUGINS_RESOURCE_PARAM = "bootstrapPluginsResource";

	String PARENT_LOCATIONS = "parentLocations";
	
	String PLUGIN_LOCATIONS_RESOURCE_PARAM = "pluginLocationsResource";
	
	/** Default config location for the root context */
	String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
	
	/** Default prefix for building a config location for a namespace */
	String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
	
	/** Default suffix for building a config location for a namespace */
	String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

	String CONTEXT_LOADER_CLASS_NAME = "contextLoaderClassName";

}

