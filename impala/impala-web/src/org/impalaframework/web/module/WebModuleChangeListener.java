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

package org.impalaframework.web.module;

import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.bootstrap.ModuleManagementFacade;
import org.impalaframework.module.monitor.BaseModuleChangeListener;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleContentChangeListener;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.web.helper.ImpalaServletUtils;
import org.springframework.web.context.ServletContextAware;

public class WebModuleChangeListener extends BaseModuleChangeListener implements
		ModuleContentChangeListener, ServletContextAware {

	private static final Log logger = LogFactory.getLog(WebModuleChangeListener.class);

	private ServletContext servletContext;

	public WebModuleChangeListener() {
		super();
	}

	public void moduleContentsModified(ModuleChangeEvent event) {
		Set<String> modified = getModifiedModules(event);

		if (!modified.isEmpty()) {
			ModuleManagementFacade factory = ImpalaServletUtils.getModuleManagementFacade(servletContext);

			for (String moduleName : modified) {

				logger.info("Processing modified module " + moduleName);

				ModuleOperation operation = factory.getModuleOperationRegistry().getOperation(ModuleOperationConstants.ReloadNamedModuleOperation);
				ModuleOperationInput moduleOperationInput = new ModuleOperationInput(null, null, moduleName);
				operation.execute(moduleOperationInput);
			}
		}
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
