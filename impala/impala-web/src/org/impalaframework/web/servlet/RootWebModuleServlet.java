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

import java.util.List;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleChangeInfo;
import org.impalaframework.module.monitor.ModuleContentChangeListener;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.module.WebRootModuleDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>
 * Impala servlet defined in web.xml, whose config locations are also defined in
 * web.xml. Unlike <code>InternalModuleServlet</code>,
 * <code>ExternalModuleServlet</code> and
 * <code>InternalFrameworkIntegrationServlet</code>, the module is loaded
 * with which this servlet instance is associated is loaded from within the
 * servlet code using the overridden <code>createWebApplicationContext</code>.
 * This is more in keeping with the traditional Spring MVC way of loading
 * servlet specific application contexts, although not nearly as flexible as
 * allowing the module to be loaded as other modules are, or even to allow the
 * servlet to be defined within the module itself.
 * </p>
 * 
 * <p>
 * This servlet instance will create a module of type <code>WEB_ROOT</code>.
 * </p>
 * 
 * <p>
 * This class may be deprecated in the future, as I am leaning strongly towards
 * using mechanisms which rely as little as possible on configuration within the
 * <code>web.xml</code>.
 * 
 * @author Phil Zoio
 */
public class RootWebModuleServlet extends BaseImpalaServlet implements ModuleContentChangeListener {

	//FIXME determine whether to deprecate this class
	private static final Log logger = LogFactory.getLog(RootWebModuleServlet.class);

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

			ModuleOperation operation = factory.getModuleOperationRegistry().getOperation(
					ModuleOperationConstants.AddModuleOperation);
			operation.execute(new ModuleOperationInput(null, newDefinition, null));
		}

		ApplicationContext context = factory.getModuleStateHolder().getModuleContexts().get(moduleName);

		if (!initialized) {

			if (factory.containsBean("scheduledModuleChangeMonitor")) {
				logger.info("Registering " + getServletName() + " for module modifications");
				ModuleChangeMonitor moduleChangeMonitor = (ModuleChangeMonitor) factory
						.getBean("scheduledModuleChangeMonitor");
				moduleChangeMonitor.addModificationListener(this);
			}
			this.initialized = true;
		}
		
		return (WebApplicationContext) context;
	}

	protected ModuleDefinition newModuleDefinition(String moduleName, RootModuleDefinition rootModuleDefinition) {
		return new WebRootModuleDefinition(rootModuleDefinition, moduleName, getSpringConfigLocations());
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

	public void moduleContentsModified(ModuleChangeEvent event) {

		List<ModuleChangeInfo> modifiedModules = event.getModifiedModules();
		for (ModuleChangeInfo info : modifiedModules) {
			if (getServletName().equals(info.getModuleName())) {
				try {
					if (logger.isDebugEnabled())
						logger.debug("Re-initialising module " + info.getModuleName());
					initServletBean();
				}
				catch (Exception e) {
					logger.error("Unable to reload module " + info.getModuleName(), e);
				}
				return;
			}
		}
	}

}
