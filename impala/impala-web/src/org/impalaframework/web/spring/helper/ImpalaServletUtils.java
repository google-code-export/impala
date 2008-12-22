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

package org.impalaframework.web.spring.helper;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.helper.WebModuleUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * Class with static convenience methods for publish, accessing, and removing <code>ServletContext</code>-based state. 
 * @author Phil Zoio
 */
public abstract class ImpalaServletUtils {
	
	public static final Log logger = LogFactory.getLog(ImpalaServletUtils.class);

	public static WebApplicationContext checkIsWebApplicationContext(String servletName, ApplicationContext applicationContext) {
		if (!(applicationContext instanceof WebApplicationContext)) {
			throw new ConfigurationException("Servlet '" + servletName + "' is not backed by an application context of type " + WebApplicationContext.class.getName() + ": " + applicationContext);
		}
		return (WebApplicationContext) applicationContext;
	}
	
	public static void publishWebApplicationContext(WebApplicationContext wac, FrameworkServlet servlet) {

		String attrName = servlet.getServletContextAttributeName();
		servlet.getServletContext().setAttribute(attrName, wac);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Published WebApplicationContext of servlet '" + servlet.getServletName()
					+ "' as ServletContext attribute with name [" + attrName + "]");
		}
	}
	
	public static void unpublishWebApplicationContext(FrameworkServlet servlet) {

		String attrName = servlet.getServletContextAttributeName();
		servlet.getServletContext().removeAttribute(attrName);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Removed WebApplicationContext of servlet '" + servlet.getServletName()
					+ "' as ServletContext attribute with name [" + attrName + "]");
		}
	}

	public static void publishServlet(ServletContext servletContext, String servletName, HttpServlet servlet) {
		
		String attributeName = WebConstants.SERVLET_MODULE_ATTRIBUTE_PREFIX + servletName;
		servletContext.setAttribute(attributeName , servlet);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Published Servlet with name '" + servletName
					+ "' as ServletContext attribute with name [" + attributeName + "]");
		}
	}

	public static void unpublishFilter(ServletContext servletContext, String filterName) {
		
		String attributeName = WebConstants.FILTER_MODULE_ATTRIBUTE_PREFIX + filterName;
		servletContext.removeAttribute(attributeName);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Removed Filter with name '" + filterName
					+ "' as ServletContext attribute with name [" + attributeName + "]");
		}
	}

	public static void publishFilter(ServletContext servletContext, String filterName, Filter filter) {
		
		String attributeName = WebConstants.FILTER_MODULE_ATTRIBUTE_PREFIX + filterName;
		servletContext.setAttribute(attributeName, filter);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Published Filter with name '" + filterName
					+ "' as ServletContext attribute with name [" + attributeName + "]");
		}
	}

	public static void unpublishServlet(ServletContext servletContext, String servletName) {
		
		String attributeName = WebConstants.SERVLET_MODULE_ATTRIBUTE_PREFIX + servletName;
		servletContext.removeAttribute(attributeName);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Removed Servlet with name '" + servletName
					+ "' as ServletContext attribute with name [" + attributeName + "]");
		}
	}
	
	public static void publishRootModuleContext(ServletContext servletContext, String servletName, ApplicationContext applicationContext) {
		
		String moduleServletContextKey = WebModuleUtils.getModuleServletContextKey(servletName, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		servletContext.setAttribute(moduleServletContextKey, applicationContext);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Published application context for '" + servletName
					+ "' as ServletContext attribute with name [" + moduleServletContextKey + "]");
		}
	}

	public static void unpublishRootModuleContext(ServletContext servletContext, String servletName) {

		String moduleServletContextKey = WebModuleUtils.getModuleServletContextKey(servletName, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		servletContext.removeAttribute(moduleServletContextKey);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Published application context for '" + servletName
					+ "' as ServletContext attribute with name [" + moduleServletContextKey + "]");
		}
	}
	
	public static ApplicationContext getRootModuleContext(ServletContext servletContext, String servletName) {

		String attributeName = WebModuleUtils.getModuleServletContextKey(servletName, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		final Object attribute = servletContext.getAttribute(attributeName);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieved application context for '" + servletName + "' as ServletContext attribute with name [" + attributeName + "]: " +
					attribute);
		}
		
		return ObjectUtils.cast(attribute, ApplicationContext.class);
	}
	
}
