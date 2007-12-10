/*
 * Copyright 2007 the original author or authors.
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

package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class WebXmlBasedContextLoader extends BaseImpalaContextLoader {

	public ParentSpec getPluginSpec(ServletContext servletContext) {

		// subclasses can override to get PluginSpec more intelligently
		String[] locations = getParentLocations(servletContext);

		ParentSpec parentSpec = new SimpleParentSpec(locations);
		String pluginNameString = getPluginDefinitionString(servletContext);
		return new SingleStringPluginSpecBuilder(parentSpec, pluginNameString).getParentSpec();
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

	protected String getPluginDefinitionString(ServletContext servletContext) {
		return servletContext.getInitParameter(WebConstants.PLUGIN_NAMES_PARAM);
	}

}
