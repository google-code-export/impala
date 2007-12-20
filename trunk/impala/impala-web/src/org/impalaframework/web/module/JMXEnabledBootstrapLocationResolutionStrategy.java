package org.impalaframework.web.module;

import javax.servlet.ServletContext;

public class JMXEnabledBootstrapLocationResolutionStrategy {
	protected String[] getBootstrapContextLocations(ServletContext servletContext) {
		String[] locations = new String[] { 
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-web-bootstrap.xml",
				"META-INF/impala-jmx-bootstrap.xml",			
				"META-INF/impala-jmx-adaptor-bootstrap.xml",
				"META-INF/impala-web-jmx-bootstrap.xml" 
				};

		//FIXME test, extend from Default...
		return locations;
	}
}
