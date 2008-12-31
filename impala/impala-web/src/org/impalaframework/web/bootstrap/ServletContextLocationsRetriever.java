/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.web.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.bootstrap.ContextLocationResolver;
import org.impalaframework.config.CompositePropertySource;
import org.impalaframework.config.PropertiesHolder;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.config.SystemPropertiesPropertySource;
import org.impalaframework.util.PropertyUtils;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.config.ServletContextPropertySource;
import org.impalaframework.web.module.WebModuleUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class ServletContextLocationsRetriever {

	private static final Log logger = LogFactory.getLog(ServletContextLocationsRetriever.class);
	
	private String defaultBootstrapResource = "impala.properties";
	private final ServletContext servletContext;
	private final ContextLocationResolver delegate;
	
	public ServletContextLocationsRetriever(ServletContext servletContext, ContextLocationResolver delegate) {
		super();
		Assert.notNull(servletContext, "servletContext cannot be null");
		Assert.notNull(delegate, "ContextLocationResolver delegate cannot be null");
		this.servletContext = servletContext;
		this.delegate = delegate;
	}

	public String[] getContextLocations() {
		final ArrayList<String> contextLocations = new ArrayList<String>();
		Properties properties = getProperties();
		List<PropertySource> propertySources = getPropertySources(properties);
		delegate.addContextLocations(contextLocations, new CompositePropertySource(propertySources));

		return contextLocations.toArray(new String[0]);
	}

	protected List<PropertySource> getPropertySources(Properties properties) {
		List<PropertySource> propertySources = new ArrayList<PropertySource>();
		
		//property value sought first in system property
		propertySources.add(new SystemPropertiesPropertySource());
		
		//then in impala properties file
		propertySources.add(new StaticPropertiesPropertySource(properties));
		
		//then as servlet context init param
		propertySources.add(new ServletContextPropertySource(servletContext));
		return propertySources;
	}

	protected Properties getProperties() {
		
		String bootstrapLocationsResource = WebModuleUtils.getLocationsResourceName(servletContext,
				WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);

		ResourceLoader resourceLoader = getResourceLoader();
		Resource bootStrapResource = null;
		
		if (bootstrapLocationsResource == null) {
			bootStrapResource = resourceLoader.getResource(defaultBootstrapResource);
		}
		else {
			// figure out which resource loader to use
			bootStrapResource = resourceLoader.getResource(bootstrapLocationsResource);
		}
		Properties properties = null;
		if (bootStrapResource == null || !bootStrapResource.exists()) {
			logger.info("Unable to load locations resource from " + bootstrapLocationsResource + ".");
			properties = new Properties();
		} else { 
			properties = PropertyUtils.loadProperties(bootStrapResource);
		}
		
		PropertiesHolder.getInstance().setProperties(properties);
		
		return properties;
	}

	protected ResourceLoader getResourceLoader() {
		return new DefaultResourceLoader();
	}

	public void setDefaultBootstrapResource(String defaultResource) {
		Assert.notNull(defaultResource);
		this.defaultBootstrapResource = defaultResource;
	}

}
