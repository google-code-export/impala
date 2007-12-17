package org.impalaframework.module.web;

import javax.servlet.ServletContext;

import org.impalaframework.module.spec.ModuleDefinitionSource;

public interface ServletPluginSpecProvider {
	ModuleDefinitionSource getPluginSpecBuilder(ServletContext servletContext);
}
