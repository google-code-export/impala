package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.builder.PluginSpecBuilder;

public interface ServletPluginSpecProvider {
	PluginSpecBuilder getPluginSpecBuilder(ServletContext servletContext);
}
