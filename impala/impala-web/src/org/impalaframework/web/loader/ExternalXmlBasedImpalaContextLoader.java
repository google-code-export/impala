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

package org.impalaframework.web.loader;

import javax.servlet.ServletContext;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.bootstrap.ExternalBootstrapLocationResolutionStrategy;
import org.impalaframework.web.module.WebModuleUtils;
import org.impalaframework.web.module.WebXmlRootDefinitionBuilder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ExternalXmlBasedImpalaContextLoader extends BaseImpalaContextLoader {

	@Override
	public String[] getBootstrapContextLocations(ServletContext servletContext) {
		return new ExternalBootstrapLocationResolutionStrategy().getBootstrapContextLocations(servletContext);
	}	
	
	@Override
	public ModuleDefinitionSource getModuleDefinitionSource(ServletContext servletContext) {
		
		String locationsResourceName = WebModuleUtils.getLocationsResourceName(servletContext,
				WebConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM);

		if (locationsResourceName == null) {
			throw new ConfigurationException(
					"Unable to resolve locations resource name parameter '"
							+ WebConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM
							+ "' from either a system property or a 'context-param' entry in the web application's WEB-INF/web.xml");
		}

		ResourceLoader resourceLoader = getResourceLoader();
		Resource resource = resourceLoader.getResource(locationsResourceName);

		if (!resource.exists()) {
			throw new ConfigurationException("Module definition XML resource '" + resource.getDescription()
					+ "' does not exist");
		}

		WebXmlRootDefinitionBuilder moduleDefinitionSource = new WebXmlRootDefinitionBuilder();
		moduleDefinitionSource.setResource(resource);
		return moduleDefinitionSource;
	}

	protected ResourceLoader getResourceLoader() {
		return new DefaultResourceLoader();
	}

}
