package org.impalaframework.web.resource;

import javax.servlet.ServletContext;

import org.impalaframework.spring.resource.PathBasedResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;

public class ServletContextResourceLoader extends PathBasedResourceLoader implements ServletContextAware {

	private ServletContext servletContext;

	@Override
	protected Resource getResourceForPath(String prefix, String location, ClassLoader classLoader) {
		return new ServletContextResource(this.servletContext, prefix + location);
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
