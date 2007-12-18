package org.impalaframework.module.web;

import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;

public class ServletPluginSpec extends WebRootPluginSpec {

	private static final long serialVersionUID = 1L;

	public ServletPluginSpec(ModuleDefinition moduleDefinition, String name, String[] contextLocations) {
		super(moduleDefinition, name, contextLocations);
	}
	
	public ServletPluginSpec(ModuleDefinition moduleDefinition, String name, List<String> contextLocations) {
		super(moduleDefinition, name, contextLocations);
	}

	@Override
	public String getType() {
		return WebPluginTypes.SERVLET;
	}

}
