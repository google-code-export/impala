package org.impalaframework.web.module;

import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;

public class ServletModuleDefinition extends WebRootModuleDefinition {

	private static final long serialVersionUID = 1L;

	public ServletModuleDefinition(ModuleDefinition moduleDefinition, String name, String[] contextLocations) {
		super(moduleDefinition, name, contextLocations);
	}
	
	public ServletModuleDefinition(ModuleDefinition moduleDefinition, String name, List<String> contextLocations) {
		super(moduleDefinition, name, contextLocations);
	}

	@Override
	public String getType() {
		return WebModuleTypes.SERVLET;
	}

}
