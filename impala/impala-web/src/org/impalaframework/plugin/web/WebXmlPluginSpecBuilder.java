package org.impalaframework.plugin.web;

import org.impalaframework.plugin.builder.SuppliedPluginInfo;
import org.impalaframework.plugin.builder.XmlPluginSpecBuilder;
import org.impalaframework.plugin.spec.PluginSpec;

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
