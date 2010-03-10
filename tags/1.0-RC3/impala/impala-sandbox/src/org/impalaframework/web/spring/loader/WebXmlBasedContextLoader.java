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

package org.impalaframework.web.spring.loader;

import javax.servlet.ServletContext;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.source.SingleStringModuleDefinitionSource;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.spring.loader.BaseImpalaContextLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * Subclass of <code>BaseImpalaContextLoader</code> which supports loading module definition information from the web.xml
 * @author Phil Zoio
 */
public class WebXmlBasedContextLoader extends BaseImpalaContextLoader {

	public ModuleDefinitionSource getModuleDefinitionSource(ServletContext servletContext, ModuleManagementFacade factory) {
		// subclasses can override to get ModuleDefinition more intelligently
		String[] locations = getParentLocations(servletContext);
		String projectName = getRootProjectName(servletContext);

		RootModuleDefinition rootModuleDefinition = new SimpleRootModuleDefinition(projectName, locations);
		String moduleNameString = getModuleDefinitionString(servletContext);
		SingleStringModuleDefinitionSource moduleDefinitionSource = new SingleStringModuleDefinitionSource(
				rootModuleDefinition, moduleNameString);
		return moduleDefinitionSource;
	}

	protected String[] getParentLocations(ServletContext servletContext) {
		String[] locations = null;
		String configLocationString = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if (configLocationString != null) {
			locations = (StringUtils.tokenizeToStringArray(configLocationString,
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS));
		}
		return locations;
	}

	protected String getRootProjectName(ServletContext servletContext) {
		String rootProjectName = null;
		String configLocationString = servletContext.getInitParameter(WebConstants.ROOT_MODULE_NAME_PARAM);
		if (configLocationString != null) {
			rootProjectName = configLocationString.trim();
		}
		else {
			throw new ConfigurationException("Cannot create root module as the init-parameter '"
					+ WebConstants.ROOT_MODULE_NAME_PARAM + "' has not been specified");
		}
		return rootProjectName;
	}

	protected String getModuleDefinitionString(ServletContext servletContext) {
		return servletContext.getInitParameter(WebConstants.MODULE_NAMES_PARAM);
	}

	@Override
	public String[] getBootstrapContextLocations(ServletContext servletContext) {
		return null;
	}

}
