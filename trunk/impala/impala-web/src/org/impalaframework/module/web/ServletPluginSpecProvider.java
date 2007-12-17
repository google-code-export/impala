package org.impalaframework.module.web;

import javax.servlet.ServletContext;

import org.impalaframework.module.spec.PluginSpecProvider;

public interface ServletPluginSpecProvider {
	PluginSpecProvider getPluginSpecBuilder(ServletContext servletContext);
}
