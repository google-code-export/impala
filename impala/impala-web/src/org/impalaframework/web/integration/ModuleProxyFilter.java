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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.helper.ImpalaServletUtils;
import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperHttpServletRequest;

/**
 * <p>FIXME document
 * </p>
 * 
 * @see InternalFrameworkIntegrationFilter
 * @author Phil Zoio
 */
public class ModuleProxyFilter implements Filter {

	//FIXME test
	
	//private static final Log logger = LogFactory.getLog(ModuleProxyFilter.class);	
	
	private static final long serialVersionUID = 1L;
	
	private String modulePrefix;
	private FilterConfig filterConfig;
	
	public ModuleProxyFilter() {
		super();
	}


	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		
		//FIXME test
		modulePrefix = filterConfig.getInitParameter("modulePrefix");
	}
	
	
	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		//FIXME log environment and path variables
		
		HttpServletRequest request = ObjectUtils.cast(servletRequest, HttpServletRequest.class);
		HttpServletResponse response = ObjectUtils.cast(servletResponse, HttpServletResponse.class);
		
		String servletPath = request.getServletPath();
		ServletContext context = filterConfig.getServletContext();
		doFilter(request, response, context, servletPath, chain);
		
	}

	void doFilter(HttpServletRequest request, HttpServletResponse response,
			ServletContext context, String servletPath, FilterChain chain)
			throws ServletException, IOException {
		String moduleName = getModuleName(servletPath);
		
		Filter moduleFilter = null;
		if (moduleName != null) {
			moduleFilter = ImpalaServletUtils.getModuleFilter(context, moduleName);
			if (moduleFilter != null) {
				
				//FIXME requestwrapper should be configured via Spring				
				
				//explicitly go through service method
				HttpServletRequest wrappedRequest = wrappedRequest(request, context, moduleName);
				
				InvocationAwareFilterChain substituteChain = new InvocationAwareFilterChain();
				
				moduleFilter.doFilter(wrappedRequest, response, substituteChain);
				
				if (substituteChain.getWasInvoked()) {
					chain.doFilter(request, response);
				}
				
			} else {
				//FIXME log in debug mode
				chain.doFilter(request, response);
			}
		} else {
			//FIXME log in debug mode
			chain.doFilter(request, response);
		}
	}

	protected HttpServletRequest wrappedRequest(HttpServletRequest request, ServletContext servletContext, String moduleName) {
		return new ModuleAwareWrapperHttpServletRequest(request, moduleName, servletContext);
	}

	String getModuleName(String servletPath) {
		
		//FIXME share common code with ModuleProxyFilter
		
		String tempModuleName = (servletPath.startsWith("/") ? servletPath.substring(1) : servletPath);
		int firstSlash = tempModuleName.indexOf('/');
		if (firstSlash < 0) {
			return null;
		}
		
		String moduleName = tempModuleName.substring(0, firstSlash);
		if (modulePrefix != null) {
			moduleName = modulePrefix + moduleName;
		}
		return moduleName;
	}

	public void setModulePrefix(String modulePrefix) {
		this.modulePrefix = modulePrefix;
	}
	
}
