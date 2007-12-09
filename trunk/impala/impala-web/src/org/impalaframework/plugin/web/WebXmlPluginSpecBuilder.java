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
		
		if (WebPluginTypes.WEB_ROOT.equals(type)) {
			return new WebRootPluginSpec(pluginSpec, pluginInfo.getName(), pluginInfo.getContextLocations());
		}
		else if (WebPluginTypes.SERVLET.equals(type)) {
			return new ServletPluginSpec(pluginSpec, pluginInfo.getName(), pluginInfo.getContextLocations());
		}
		
		return super.createPluginSpec(pluginSpec, pluginInfo);
	}

}
