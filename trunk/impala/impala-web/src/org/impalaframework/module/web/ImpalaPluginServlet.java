package org.impalaframework.module.web;

import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;

public class ImpalaPluginServlet extends ImpalaRootServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected ModuleDefinition newPluginSpec(String pluginName, RootModuleDefinition rootModuleDefinition) {
		ModuleDefinition moduleDefinition = rootModuleDefinition;

		String pluginNameString = getServletContext().getInitParameter(WebConstants.ROOT_WEB_PLUGIN_PARAM);

		if (pluginNameString != null) {
			ModuleDefinition rootWebPlugin = rootModuleDefinition.findPlugin(pluginNameString, true);
			if (rootWebPlugin == null) {
				throw new IllegalStateException("Unable to find root plugin '" + pluginNameString
						+ "' specified using the web.xml parameter '" + WebConstants.ROOT_WEB_PLUGIN_PARAM + "'");
			}
			moduleDefinition = rootWebPlugin;
		}

		return new ServletPluginSpec(moduleDefinition, pluginName, getSpringConfigLocations());
	}

}
