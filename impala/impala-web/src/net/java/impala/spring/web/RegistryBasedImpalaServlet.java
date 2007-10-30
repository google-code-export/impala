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

package net.java.impala.spring.web;

import net.java.impala.spring.SpringContextHolder;
import net.java.impala.spring.plugin.ParentSpec;
import net.java.impala.spring.plugin.PluginSpec;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class RegistryBasedImpalaServlet extends DispatcherServlet {

	/** Default config location for the root context */
	public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";

	/** Default prefix for building a config location for a namespace */
	public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";

	/** Default suffix for building a config location for a namespace */
	public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

	private static final long serialVersionUID = 1L;

	// lifted straight from XmlWebApplicationContext
	protected String[] getDefaultConfigLocations() {
		if (getNamespace() != null) {
			return new String[] { DEFAULT_CONFIG_LOCATION_PREFIX + getNamespace() + DEFAULT_CONFIG_LOCATION_SUFFIX };
		}
		else {
			return new String[] { DEFAULT_CONFIG_LOCATION };
		}
	}

	@Override
	protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent) throws BeansException {
		
		//FIXME add support for refreshing ApplicationContext
		
		SpringContextHolder holder = (SpringContextHolder) getServletContext().getAttribute(RegistryBasedImpalaContextLoader.CONTEXT_HOLDER_PARAM);
		
		if (holder == null) {
			throw new RuntimeException("WebDynamicContextHolder not set. Have you set up your Impala context loader properly?");
		}
		
		ParentSpec parentSpec = holder.getParent();
		
		PluginSpec plugin = new WebServletSpec(parentSpec, getServletName(), getSpringConfigLocations());
		holder.addPlugin(plugin);
		
		ApplicationContext context = holder.getPlugins().get(plugin.getName());
		return (WebApplicationContext) context;
		
	}

	protected String[] getSpringConfigLocations() {
		String[] locations = null;
		if (getContextConfigLocation() != null) {
			locations = StringUtils.tokenizeToStringArray(getContextConfigLocation(),
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
		}
		else {
			locations = getDefaultConfigLocations();
		}
		return locations;
	}

}
