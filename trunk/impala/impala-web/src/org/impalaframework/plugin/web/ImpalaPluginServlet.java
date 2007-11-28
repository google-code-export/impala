package org.impalaframework.plugin.web;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;

public class ImpalaPluginServlet extends ImpalaRootServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected PluginSpec newPluginSpec(String pluginName, ParentSpec parentSpec) {
		// FIXME test
		PluginSpec pluginSpec = parentSpec;

		String pluginNameString = getServletContext().getInitParameter(WebConstants.ROOT_WEB_PLUGIN_PARAM);

		if (pluginNameString != null) {
			PluginSpec rootWebPlugin = parentSpec.findPlugin(pluginNameString, true);
			if (rootWebPlugin == null) {
				throw new IllegalStateException("FIXME");
			}
			pluginSpec = rootWebPlugin;
		}

		return new ServletPluginSpec(pluginSpec, pluginName, getSpringConfigLocations());
	}

}
