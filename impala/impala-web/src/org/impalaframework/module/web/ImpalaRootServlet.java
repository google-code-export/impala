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

package org.impalaframework.module.web;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.monitor.PluginModificationListener;
import org.impalaframework.module.monitor.PluginMonitor;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class ImpalaRootServlet extends BaseImpalaServlet implements PluginModificationListener {

	final Logger logger = LoggerFactory.getLogger(ImpalaRootServlet.class);

	private static final long serialVersionUID = 1L;

	private boolean initialized;

	public ImpalaRootServlet() {
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

		ModuleManagementSource factory = (ModuleManagementSource) getServletContext().getAttribute(
				WebConstants.IMPALA_FACTORY_ATTRIBUTE);

		if (factory == null) {
			throw new RuntimeException(ModuleManagementSource.class.getSimpleName()
					+ " not set. Have you set up your Impala context loader properly? "
					+ "You need to set up a Spring context loader which will set up the parameter '"
					+ WebConstants.IMPALA_FACTORY_ATTRIBUTE + "'");
		}


		String pluginName = getServletName();
		if (!initialized) {

			PluginStateManager pluginStateManager = factory.getPluginStateManager();
			RootModuleDefinition existing = pluginStateManager.getParentSpec();
			RootModuleDefinition newSpec = pluginStateManager.cloneParentSpec();
			newPluginSpec(pluginName, newSpec);

			//FIXME this should be deprecated!
			ModuleModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry()
					.getPluginModificationCalculator(ModificationCalculationType.STRICT);
			ModuleTransitionSet transitions = calculator.getTransitions(existing, newSpec);

			pluginStateManager.processTransitions(transitions);

		}

		ApplicationContext context = factory.getPluginStateManager().getPlugins().get(pluginName);

		if (factory.containsBean("scheduledPluginMonitor") && !initialized) {
			logger.info("Registering " + getServletName() + " for plugin modifications");
			PluginMonitor pluginMonitor = (PluginMonitor) factory.getBean("scheduledPluginMonitor");
			pluginMonitor.addModificationListener(this);
		}

		this.initialized = true;

		return (WebApplicationContext) context;
	}

	protected ModuleDefinition newPluginSpec(String pluginName, RootModuleDefinition rootModuleDefinition) {
		return new WebRootPluginSpec(rootModuleDefinition, pluginName, getSpringConfigLocations());
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
