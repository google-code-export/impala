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

package org.impalaframework.web.helper;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.WebConstants;
/**
 * Class with static convenience methods for publish, accessing, and removing <code>ServletContext</code>-based state. 
 * @author Phil Zoio
 */
public abstract class WebModuleUtils {
	
	private static final Log logger = LogFactory.getLog(WebModuleUtils.class);

	public static ModuleManagementFacade getModuleManagementFacade(ServletContext servletContext) {
		final String attributeName = WebConstants.IMPALA_FACTORY_ATTRIBUTE;
		final Object attribute = servletContext.getAttribute(attributeName);
		
		if (WebModuleUtils.logger.isDebugEnabled()) {
			WebModuleUtils.logger.debug("Retrieved ModuleManagementFacade from ServletContext with attribute name '" + attributeName + "': " + attribute);
		}
		
		return ObjectUtils.cast(attribute, ModuleManagementFacade.class);
	}

	public static HttpServlet getModuleServlet(ServletContext servletContext, String moduleName) {
		final String attributeName = WebConstants.SERVLET_MODULE_ATTRIBUTE_PREFIX + moduleName;
		final Object attribute = servletContext.getAttribute(attributeName);
		
		if (WebModuleUtils.logger.isDebugEnabled()) {
			WebModuleUtils.logger.debug("Retrieved module Servlet from ServletContext with attribute name '" + attributeName + "': " + attribute);
		}
		
		return ObjectUtils.cast(attribute, HttpServlet.class);
	}

	public static Filter getModuleFilter(ServletContext servletContext, String moduleName) {
		final String attributeName = WebConstants.FILTER_MODULE_ATTRIBUTE_PREFIX + moduleName;
		final Object attribute = servletContext.getAttribute(attributeName);
		
		if (WebModuleUtils.logger.isDebugEnabled()) {
			WebModuleUtils.logger.debug("Retrieved module Filter from ServletContext with attribute name '" + attributeName + "': " + attribute);
		}
		
		return ObjectUtils.cast(attribute, Filter.class);
	}

	public static String getModuleServletContextKey(String moduleName, String attributeName) {
		return "module_" + moduleName + ":" + attributeName;
	}

	
	
}
