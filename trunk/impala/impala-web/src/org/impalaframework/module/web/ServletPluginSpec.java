package org.impalaframework.module.web;

import java.util.List;

import org.impalaframework.module.spec.PluginSpec;

public class ServletPluginSpec extends WebRootPluginSpec {

	private static final long serialVersionUID = 1L;

	public ServletPluginSpec(PluginSpec pluginSpec, String name, String[] contextLocations) {
		super(pluginSpec, name, contextLocations);
	}
	
	public ServletPluginSpec(PluginSpec pluginSpec, String name, List<String> contextLocations) {
		super(pluginSpec, name, contextLocations);
	}

	@Override
	public String getType() {
		return WebPluginTypes.SERVLET;
	}

}
