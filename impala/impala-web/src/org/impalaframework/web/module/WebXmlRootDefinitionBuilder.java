package org.impalaframework.web.module;

import org.impalaframework.module.builder.SuppliedModuleDefinitionInfo;
import org.impalaframework.module.builder.XmlModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;

public class WebXmlRootDefinitionBuilder extends XmlModuleDefinitionSource {

	public WebXmlRootDefinitionBuilder() {
		super();
	}

	@Override
	protected ModuleDefinition createPluginSpec(ModuleDefinition moduleDefinition, SuppliedModuleDefinitionInfo pluginInfo) {
		
		String type = pluginInfo.getType();
		
		if (WebModuleTypes.WEB_ROOT.equalsIgnoreCase(type)) {
			return new WebRootModuleDefinition(moduleDefinition, pluginInfo.getName(), pluginInfo.getContextLocations());
		}
		else if (WebModuleTypes.SERVLET.equalsIgnoreCase(type)) {
			return new ServletModuleDefinition(moduleDefinition, pluginInfo.getName(), pluginInfo.getContextLocations());
		}
		else if (WebModuleTypes.WEB_PLACEHOLDER.equalsIgnoreCase(type)) {
			return new WebPlaceholderModuleDefinition(moduleDefinition, pluginInfo.getName());
		}
		
		return super.createPluginSpec(moduleDefinition, pluginInfo);
	}

}
