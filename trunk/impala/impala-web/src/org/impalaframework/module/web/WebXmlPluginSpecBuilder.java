package org.impalaframework.module.web;

import org.impalaframework.module.builder.SuppliedPluginInfo;
import org.impalaframework.module.builder.XmlPluginSpecBuilder;
import org.impalaframework.module.spec.PluginSpec;

public class WebXmlPluginSpecBuilder extends XmlPluginSpecBuilder {

	public WebXmlPluginSpecBuilder() {
		super();
	}

	@Override
	protected PluginSpec createPluginSpec(PluginSpec pluginSpec, SuppliedPluginInfo pluginInfo) {
		
		String type = pluginInfo.getType();
		
		if (WebPluginTypes.WEB_ROOT.equalsIgnoreCase(type)) {
			return new WebRootPluginSpec(pluginSpec, pluginInfo.getName(), pluginInfo.getContextLocations());
		}
		else if (WebPluginTypes.SERVLET.equalsIgnoreCase(type)) {
			return new ServletPluginSpec(pluginSpec, pluginInfo.getName(), pluginInfo.getContextLocations());
		}
		else if (WebPluginTypes.WEB_PLACEHOLDER.equalsIgnoreCase(type)) {
			return new WebPlaceholderPluginSpec(pluginSpec, pluginInfo.getName());
		}
		
		return super.createPluginSpec(pluginSpec, pluginInfo);
	}

}
