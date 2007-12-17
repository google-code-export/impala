package org.impalaframework.module.web;

import org.impalaframework.module.builder.SuppliedPluginInfo;
import org.impalaframework.module.builder.XmlPluginSpecBuilder;
import org.impalaframework.module.spec.ModuleDefinition;

public class WebXmlPluginSpecBuilder extends XmlPluginSpecBuilder {

	public WebXmlPluginSpecBuilder() {
		super();
	}

	@Override
	protected ModuleDefinition createPluginSpec(ModuleDefinition moduleDefinition, SuppliedPluginInfo pluginInfo) {
		
		String type = pluginInfo.getType();
		
		if (WebPluginTypes.WEB_ROOT.equalsIgnoreCase(type)) {
			return new WebRootPluginSpec(moduleDefinition, pluginInfo.getName(), pluginInfo.getContextLocations());
		}
		else if (WebPluginTypes.SERVLET.equalsIgnoreCase(type)) {
			return new ServletPluginSpec(moduleDefinition, pluginInfo.getName(), pluginInfo.getContextLocations());
		}
		else if (WebPluginTypes.WEB_PLACEHOLDER.equalsIgnoreCase(type)) {
			return new WebPlaceholderPluginSpec(moduleDefinition, pluginInfo.getName());
		}
		
		return super.createPluginSpec(moduleDefinition, pluginInfo);
	}

}
