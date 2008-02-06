package org.impalaframework.web.module;

import org.impalaframework.module.builder.SuppliedModuleDefinitionInfo;
import org.impalaframework.module.builder.XmlModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;

public class WebXmlRootDefinitionBuilder extends XmlModuleDefinitionSource {

	public WebXmlRootDefinitionBuilder() {
		super();
	}

	@Override
	protected ModuleDefinition createModuleDefinition(ModuleDefinition moduleDefinition, SuppliedModuleDefinitionInfo info) {
		
		String type = info.getType();
		
		if (WebModuleTypes.WEB_ROOT.equalsIgnoreCase(type)) {
			return new WebRootModuleDefinition(moduleDefinition, info.getName(), info.getContextLocations());
		}
		else if (WebModuleTypes.SERVLET.equalsIgnoreCase(type)) {
			return new ServletModuleDefinition(moduleDefinition, info.getName(), info.getContextLocations());
		}
		else if (WebModuleTypes.WEB_PLACEHOLDER.equalsIgnoreCase(type)) {
			return new WebPlaceholderModuleDefinition(moduleDefinition, info.getName());
		}
		
		return super.createModuleDefinition(moduleDefinition, info);
	}

}
