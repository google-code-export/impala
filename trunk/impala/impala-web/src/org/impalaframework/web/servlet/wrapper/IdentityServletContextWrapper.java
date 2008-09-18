package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinition;

/**
 * Trivial implementation of <code>ServletContextWrapper</code>
 * @author Phil Zoio
 */
public class IdentityServletContextWrapper implements ServletContextWrapper {

	public ServletContext wrapServletContext(ServletContext servletContext,
			ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		System.out.println("Returning " + servletContext);
		return servletContext;
	}

}
