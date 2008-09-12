package org.impalaframework.web.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

public class ImpalaServletUtils {

	private static final Log logger = LogFactory.getLog(ImpalaServletUtils.class);
	
	public static WebApplicationContext initWithContext(FrameworkServlet servlet, WebApplicationContext wac) {

		// Publish the context as a servlet context attribute.
		String attrName = servlet.getServletContextAttributeName();
		servlet.getServletContext().setAttribute(attrName, wac);
		if (logger.isDebugEnabled()) {
			logger.debug("Published WebApplicationContext of servlet '" + servlet.getServletName()
					+ "' as ServletContext attribute with name [" + attrName + "]");
		}

		return wac;
	}
}
