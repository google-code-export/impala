/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
