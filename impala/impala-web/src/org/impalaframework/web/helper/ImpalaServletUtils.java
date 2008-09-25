package org.impalaframework.web.helper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.bootstrap.ModuleManagementFacade;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.WebConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

public class ImpalaServletUtils {

	private static final Log logger = LogFactory.getLog(ImpalaServletUtils.class);

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

	public static void unpublishServlet(ServletContext servletContext, String servletName) {
		String attributeName = WebConstants.SERVLET_MODULE_ATTRIBUTE_PREFIX + servletName;
		servletContext.removeAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE_PREFIX + servletName);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Removed Servlet with name '" + servletName
					+ "' as ServletContext attribute with name [" + attributeName + "]");
		}
	}
	
	public static void publishRootModuleContext(ServletContext servletContext, String servletName, ApplicationContext applicationContext) {
		
		String moduleServletContextKey = getModuleServletContextKey(servletName, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		servletContext.setAttribute(moduleServletContextKey, applicationContext);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Published application context for '" + servletName
					+ "' as ServletContext attribute with name [" + moduleServletContextKey + "]");
		}
	}

	public static void unpublishRootModuleContext(ServletContext servletContext, String servletName) {
		
		String moduleServletContextKey = getModuleServletContextKey(servletName, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		servletContext.removeAttribute(moduleServletContextKey);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Published application context for '" + servletName
					+ "' as ServletContext attribute with name [" + moduleServletContextKey + "]");
		}
	}
	
	public static String getModuleServletContextKey(String moduleName, String attributeName) {
		return "module_" + moduleName + ":" + attributeName;
	}
	
	public static HttpServlet getModuleServlet(ServletContext servletContext, String moduleName) {
		return ObjectUtils.cast(servletContext.getAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE_PREFIX + moduleName), HttpServlet.class);
	}
	
	public static ModuleManagementFacade getModuleManagementFactory(ServletContext servletContext) {
		return ObjectUtils.cast(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE), ModuleManagementFacade.class);
	}
	
}
