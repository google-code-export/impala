package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.spec.PluginSpecProvider;

public interface ServletPluginSpecProvider {
	PluginSpecProvider getPluginSpecBuilder(ServletContext servletContext);
}
