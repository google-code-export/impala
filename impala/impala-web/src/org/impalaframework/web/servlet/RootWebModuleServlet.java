/*
 * Copyright 2007 the original author or authors.
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

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.monitor.ModuleContentChangeListener;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.module.WebRootModuleDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class RootWebModuleServlet extends BaseImpalaServlet implements ModuleContentChangeListener {

	final Logger logger = LoggerFactory.getLogger(RootWebModuleServlet.class);

	private static final long serialVersionUID = 1L;

	private boolean initialized;

	public RootWebModuleServlet() {
		super();
	}

	// lifted straight from XmlWebApplicationContext
	protected String[] getDefaultConfigLocations() {
		String nameSpace = getNamespace();
		if (nameSpace != null) {
			return new String[] { WebConstants.DEFAULT_CONFIG_LOCATION_PREFIX + nameSpace
					+ WebConstants.DEFAULT_CONFIG_LOCATION_SUFFIX };
		}
		else {
			return new String[] { WebConstants.DEFAULT_CONFIG_LOCATION };
		}
	}

	protected WebApplicationContext createWebApplicationContext() throws BeansException {

		ModuleManagementFactory factory = (ModuleManagementFactory) getServletContext().getAttribute(
				WebConstants.IMPALA_FACTORY_ATTRIBUTE);

		if (factory == null) {
			throw new RuntimeException(ModuleManagementFactory.class.getSimpleName()
					+ " not set. Have you set up your Impala context loader properly? "
					+ "You need to set up a Spring context loader which will set up the parameter '"
					+ WebConstants.IMPALA_FACTORY_ATTRIBUTE + "'");
		}


		String moduleName = getServletName();
		if (!initialized) {

			ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
			RootModuleDefinition rootDefinition = moduleStateHolder.cloneRootModuleDefinition();
			ModuleDefinition newDefinition = newModuleDefinition(moduleName, rootDefinition);
			
			ModuleOperation operation = factory.getModuleOperationRegistry().getOperation(ModuleOperationConstants.AddModuleOperation);
			operation.execute(new ModuleOperationInput(null, newDefinition, null));

		}

		ApplicationContext context = factory.getModuleStateHolder().getModuleContexts().get(moduleName);

		if (factory.containsBean("scheduledPluginMonitor") && !initialized) {
			logger.info("Registering " + getServletName() + " for plugin modifications");
			ModuleChangeMonitor moduleChangeMonitor = (ModuleChangeMonitor) factory.getBean("scheduledPluginMonitor");
			moduleChangeMonitor.addModificationListener(this);
		}

		this.initialized = true;

		return (WebApplicationContext) context;
	}

	protected ModuleDefinition newModuleDefinition(String pluginName, RootModuleDefinition rootModuleDefinition) {
		return new WebRootModuleDefinition(rootModuleDefinition, pluginName, getSpringConfigLocations());
	}

	protected String[] getSpringConfigLocations() {
		String[] locations = null;
		if (getContextConfigLocation() != null) {
			locations = StringUtils.tokenizeToStringArray(getContextConfigLocation(),
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
		}
		else {
			locations = getDefaultConfigLocations();
		}
		return locations;
	}

}
