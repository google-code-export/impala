package org.impalaframework.plugin.web;

import org.impalaframework.plugin.spec.PluginSpec;

public class ServletPluginSpec extends WebRootPluginSpec {

	private static final long serialVersionUID = 1L;

	public ServletPluginSpec(PluginSpec pluginSpec, String name, String[] contextLocations) {
		super(pluginSpec, name, contextLocations);
	}

	@Override
	public String getType() {
		return WebPluginTypes.SERVLET;
	}

}
