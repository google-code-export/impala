package org.impalaframework.plugin.web;

import org.impalaframework.plugin.spec.ParentSpec;

public class ServletPluginSpec extends WebRootPluginSpec {

	private static final long serialVersionUID = 1L;

	public ServletPluginSpec(ParentSpec parentSpec, String name, String[] contextLocations) {
		super(parentSpec, name, contextLocations);
	}

	@Override
	public String getType() {
		return WebPluginTypes.SERVLET;
	}

}
