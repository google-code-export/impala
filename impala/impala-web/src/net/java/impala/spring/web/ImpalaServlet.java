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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.DispatcherServlet;

public class ImpalaServlet extends DispatcherServlet {

	/** Default config location for the root context */
	public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";

	/** Default prefix for building a config location for a namespace */
	public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";

	/** Default suffix for building a config location for a namespace */
	public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

	private static final long serialVersionUID = 1L;

	private File contextDirectory;

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

		String parentName = getServletContext().getInitParameter("parentName");

		if (parentName == null) {
			throw new IllegalStateException("'parentName' parameter not set");
		}
		
		WebDynamicContextHolder holder = (WebDynamicContextHolder) getServletContext().getAttribute(ImpalaContextLoader.CONTEXT_HOLDER_PARAM);
		
		if (holder == null) {
			throw new RuntimeException("WebDynamicContextHolder not set. Have you set up your Impala context loader properly?");
		}

		WebApplicationContextLoader applicationContextLoader = holder.getApplicationContextLoader();
		
		//FIXME move this out
		setContextDir(parentName, applicationContextLoader);
		
		return applicationContextLoader.loadWebContext(parent, parentName, getServletName(), getServletContext(), getResourceLocations());
		
	}

	private void setContextDir(String parentName, WebApplicationContextLoader applicationContextLoader) {
		Resource resource = applicationContextLoader.getWebContextResourceHelper().getParentWebClassLocation(parentName, getServletName());
		try {
			this.contextDirectory = resource.getFile();
		}
		catch (IOException e) {
			//FIXME make context directory point to a Resource anyway
			e.printStackTrace();
		}
	}

	protected File getContextDirectory() {
		return contextDirectory;
	}
	
	protected List<Resource> getResourceLocations() {
		List<Resource> resources = new ArrayList<Resource>();
		
		String[] locations = null;
		if (getContextConfigLocation() != null) {
			locations = StringUtils.tokenizeToStringArray(getContextConfigLocation(),
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
		}
		else {
			locations = getDefaultConfigLocations();
		}

		for (String location : locations) {
			ServletContextResource resource = new ServletContextResource(getServletContext(), location);
			resources.add(resource);
		}
		return resources;
	}

}
