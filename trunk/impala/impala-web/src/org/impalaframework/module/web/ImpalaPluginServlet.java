package org.impalaframework.module.web;

import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpec;

public class ImpalaPluginServlet extends ImpalaRootServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected PluginSpec newPluginSpec(String pluginName, ParentSpec parentSpec) {
		PluginSpec pluginSpec = parentSpec;

		String pluginNameString = getServletContext().getInitParameter(WebConstants.ROOT_WEB_PLUGIN_PARAM);

		if (pluginNameString != null) {
			PluginSpec rootWebPlugin = parentSpec.findPlugin(pluginNameString, true);
			if (rootWebPlugin == null) {
				throw new IllegalStateException("Unable to find root plugin '" + pluginNameString
						+ "' specified using the web.xml parameter '" + WebConstants.ROOT_WEB_PLUGIN_PARAM + "'");
			}
			pluginSpec = rootWebPlugin;
		}

		return new ServletPluginSpec(pluginSpec, pluginName, getSpringConfigLocations());
	}

}
