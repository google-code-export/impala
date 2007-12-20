package org.impalaframework.module.web;

import javax.servlet.ServletContext;

public class DefaultBootstrapLocationResolutionStrategy {
	public String[] getBootstrapContextLocations(ServletContext servletContext) {
		String[] locations = new String[] { 
				"META-INF/impala-bootstrap.xml",
				"META-INF/impala-web-bootstrap.xml"
				};
		
		//FIXME test impala-web-jmx-bootstrap
		return locations;
	}
}
