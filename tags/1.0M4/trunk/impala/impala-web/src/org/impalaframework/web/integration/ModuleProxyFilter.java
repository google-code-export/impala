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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.helper.WebServletUtils;
import org.impalaframework.web.integration.InvocationAwareFilterChain;
import org.impalaframework.web.integration.ModuleProxyUtils;
import org.impalaframework.web.integration.RequestModuleMapper;

/**
 * <p><code>Filter</code> which performs a similar function to <code>ModuleProxyServlet</code>
 * </p>
 * 
 * @see InternalFrameworkIntegrationFilter
 * @see ModuleProxyServlet
 * @author Phil Zoio
 */
public class ModuleProxyFilter implements Filter {
	
	private static final Log logger = LogFactory.getLog(ModuleProxyFilter.class);	
	
	private static final long serialVersionUID = 1L;
	
	private FilterConfig filterConfig;
	
	private RequestModuleMapper requestModuleMapper;
	
	public ModuleProxyFilter() {
		super();
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.requestModuleMapper = newRequestModuleMapper(filterConfig);
		this.requestModuleMapper.init(filterConfig);
	}

	protected RequestModuleMapper newRequestModuleMapper(FilterConfig filterConfig) {
		final String requestModuleMapperClass = filterConfig.getInitParameter(WebConstants.REQUEST_MODULE_MAPPER_CLASS_NAME);
		return ModuleProxyUtils.newRequestModuleMapper(requestModuleMapperClass);
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = ObjectUtils.cast(servletRequest, HttpServletRequest.class);
		HttpServletResponse response = ObjectUtils.cast(servletResponse, HttpServletResponse.class);

		ModuleProxyUtils.maybeLogRequest(request, logger);
		
		ServletContext context = filterConfig.getServletContext();
		doFilter(request, response, context, chain);
		
	}

	void doFilter(HttpServletRequest request, HttpServletResponse response,
			ServletContext context, FilterChain chain)
			throws ServletException, IOException {

		String moduleName = getModuleName(request);
		
		Filter moduleFilter = null;
		if (moduleName != null) {
			moduleFilter = WebServletUtils.getModuleFilter(context, moduleName);
			if (moduleFilter != null) {
				
				if (logger.isDebugEnabled()) {
					logger.debug("Found module filter [" + moduleFilter + "] for module name [" + moduleName + "]");
				}
				
				//explicitly go through service method
				HttpServletRequest wrappedRequest = wrappedRequest(request, context, moduleName);
				
				InvocationAwareFilterChain substituteChain = new InvocationAwareFilterChain();
				
				moduleFilter.doFilter(wrappedRequest, response, substituteChain);
				
				if (substituteChain.getWasInvoked()) {
					
					if (logger.isDebugEnabled()) {
						logger.debug("Filter [" + moduleFilter + "] did not process request. Chaining request.");
					}
					chain.doFilter(request, response);
					
				} else {
					
					if (logger.isDebugEnabled()) {
						logger.debug("Filter [" + moduleFilter + "] completed processing of request.");
					}
				}
				
			} else {
				
				if (logger.isDebugEnabled()) {
					logger.debug("No module filter found for candidate module name [" + moduleName + "]. Chaining request.");
				}
				chain.doFilter(request, response);
				
			}
		} else {
			
			if (logger.isDebugEnabled()) {
				logger.debug("Path + '" + request.getRequestURI() + "' does not correspond with any candidate module name. Chaining request.");
			}
			chain.doFilter(request, response);
			
		}
	}

	String getModuleName(HttpServletRequest request) {
		String moduleName = requestModuleMapper.getModuleForRequest(request);
		return moduleName;
	}
	
	/* **************** protected methods ******************* */

	protected HttpServletRequest wrappedRequest(HttpServletRequest request, ServletContext servletContext, String moduleName) {
		return ModuleIntegrationUtils.getWrappedRequest(request, servletContext, moduleName);
	}
	
	/* **************** package level getters ******************* */

	FilterConfig getFilterConfig() {
		return filterConfig;
	}
}
