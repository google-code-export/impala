package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.spec.ParentSpec;

public interface ServletPluginSpecProvider {
	ParentSpec getPluginSpec(ServletContext servletContext);
}
