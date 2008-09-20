package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinition;

public class ModuleAwareServletContextWrapper implements ServletContextWrapper {

	public ServletContext wrapServletContext(ServletContext servletContext,
			ModuleDefinition moduleDefinition, 
			ClassLoader classLoader) {
		
		return new ModuleAwareWrapperServletContext(servletContext, moduleDefinition.getName(), classLoader);
	}

}
