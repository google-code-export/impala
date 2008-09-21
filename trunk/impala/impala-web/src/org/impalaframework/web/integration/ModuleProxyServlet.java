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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.helper.ImpalaServletUtils;
import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperHttpServletRequest;
import org.springframework.web.servlet.HttpServletBean;

/**
 * Servlet who's job it is to figure out the mapping to a particular request and redirect this to the correct module servlet instance
 * @author Phil Zoio
 */
public class ModuleProxyServlet extends HttpServletBean {

	private static final long serialVersionUID = 1L;
	
	private String modulePrefix;
	
	public ModuleProxyServlet() {
		super();
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//FIXME test entire method
		
		if (logger.isDebugEnabled()) {
			logger.debug("Request context path: " + request.getContextPath());
			logger.debug("Request local address: " + request.getLocalAddr());
			logger.debug("Request local name: " + request.getLocalName());
			logger.debug("Request path info: " + request.getPathInfo());
			logger.debug("Request path translated: " + request.getPathTranslated());
			logger.debug("Request query string: " + request.getQueryString());
			logger.debug("Request servlet path: " + request.getServletPath());
			logger.debug("Request request URI: " + request.getRequestURI());
			logger.debug("Request request URL: " + request.getRequestURL());
			logger.debug("Request session ID: " + request.getRequestedSessionId());
		}
		
		String servletPath = request.getServletPath();
		String moduleName = getModuleName(servletPath);
		ServletContext context = getServletContext();
		
		HttpServlet moduleServlet = null;
		if (moduleName != null) {
			moduleServlet = ImpalaServletUtils.getModuleServlet(context, moduleName);
			if (moduleServlet != null) {
				//explicitly go through service method
				HttpServletRequest wrappedRequest = wrappedRequest(request, context, moduleName);
				moduleServlet.service(wrappedRequest, response);
			} else {
				logger.warn("No redirection possible for servlet path " + servletPath + ", module name " + moduleName);
			}
		} else {
			logger.warn("Not possible to figure out module name from servlet path " + servletPath);
		}
		
	}

	protected HttpServletRequest wrappedRequest(HttpServletRequest request, ServletContext servletContext, String moduleName) {
		return new ModuleAwareWrapperHttpServletRequest(request, moduleName, servletContext);
	}

	String getModuleName(String servletPath) {
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
