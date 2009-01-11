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

package org.impalaframework.web.integration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

/**
 * Implementation of {@link RequestModuleMapper} which uses the 
 * @author Phil Zoio
 */
public class ServletPathRequestModuleMapper implements RequestModuleMapper {

	private String prefix;

	public void init(ServletConfig servletConfig) {		
		Assert.notNull(servletConfig);
		this.prefix = servletConfig.getInitParameter("modulePrefix");
	}

	public void init(FilterConfig filterConfig) {
		Assert.notNull(filterConfig);
		this.prefix = filterConfig.getInitParameter("modulePrefix");
	}
	
	public String getModuleForRequest(HttpServletRequest request) {
		String moduleName = getModuleName(request.getServletPath(), prefix);
		return moduleName;
	}

	String getModuleName(String servletPath, String modulePrefix) {
		
		String moduleName = ModuleProxyUtils.getTopLevelPathSegment(servletPath);
		
		if (moduleName == null) {
			return null;
		}
		
		if (modulePrefix != null) {
			moduleName = modulePrefix + moduleName;
		}
		return moduleName;
	}

}
