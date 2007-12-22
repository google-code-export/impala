package org.impalaframework.web.bootstrap;

import javax.servlet.ServletContext;

public class DefaultBootstrapLocationResolutionStrategy {
	public String[] getBootstrapContextLocations(ServletContext servletContext) {
		String[] locations = new String[] { 
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-web-bootstrap.xml"
				};
		return locations;
	}
}
