package org.impalaframework.web.module;

import javax.servlet.ServletContext;

public class WebModuleUtils {

	public static String getLocationsResourceName(ServletContext servletContext, String paramName) {
		// first look for System property which contains plugins definitions
		// location
		String resourceName = System.getProperty(paramName);
	
		if (resourceName == null) {
			resourceName = servletContext
					.getInitParameter(paramName);
		}
		return resourceName;
	}

}
