package org.impalaframework.web.servlet;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.module.ServletModuleDefinition;

public class WebModuleServlet extends RootWebModuleServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected ModuleDefinition newModuleDefinition(String moduleName, RootModuleDefinition rootModuleDefinition) {
		ModuleDefinition moduleDefinition = rootModuleDefinition;

		String moduleNameString = getServletContext().getInitParameter(WebConstants.ROOT_WEB_MODULE_PARAM);

		if (moduleNameString != null) {
			ModuleDefinition rootWebModule = rootModuleDefinition.findChildDefinition(moduleNameString, true);
			if (rootWebModule == null) {
				throw new ConfigurationException("Unable to find root module '" + moduleNameString
						+ "' specified using the web.xml parameter '" + WebConstants.ROOT_WEB_MODULE_PARAM + "'");
			}
			moduleDefinition = rootWebModule;
		}

		return new ServletModuleDefinition(moduleDefinition, moduleName, getSpringConfigLocations());
	}

}
