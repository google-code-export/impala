package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinition;

public interface ServletContextWrapper {

	ServletContext wrapServletContext(ServletContext servletContext, ModuleDefinition moduleDefinition, ClassLoader classLoader);

}
