package org.impalaframework.web.servlet;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.module.ServletModuleDefinition;

public class WebModuleServlet extends RootWebModuleServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected ModuleDefinition newPluginSpec(String pluginName, RootModuleDefinition rootModuleDefinition) {
		ModuleDefinition moduleDefinition = rootModuleDefinition;

		String pluginNameString = getServletContext().getInitParameter(WebConstants.ROOT_WEB_MODULE_PARAM);

		if (pluginNameString != null) {
			ModuleDefinition rootWebModule = rootModuleDefinition.findChildDefinition(pluginNameString, true);
			if (rootWebModule == null) {
				throw new IllegalStateException("Unable to find root plugin '" + pluginNameString
						+ "' specified using the web.xml parameter '" + WebConstants.ROOT_WEB_MODULE_PARAM + "'");
			}
			moduleDefinition = rootWebModule;
		}

		return new ServletModuleDefinition(moduleDefinition, pluginName, getSpringConfigLocations());
	}

}
