package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

public class AutoReloadingBootstrapLocationResolutionStrategy {
	protected String[] getBootstrapContextLocations(ServletContext servletContext) {
		String[] locations = new String[] { 
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-web-bootstrap.xml",
				"META-INF/impala-jmx-bootstrap.xml",				
				"META-INF/impala-web-listener-bootstrap.xml" 
				};

		//FIXME test, extend from Default...
		return locations;
	}
}
