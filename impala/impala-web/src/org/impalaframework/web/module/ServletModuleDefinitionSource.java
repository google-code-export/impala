package org.impalaframework.web.module;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinitionSource;

public interface ServletModuleDefinitionSource {
	ModuleDefinitionSource getPluginSpecBuilder(ServletContext servletContext);
}
