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

package org.impalaframework.plugin.web;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.monitor.PluginModificationEvent;
import org.impalaframework.plugin.monitor.PluginModificationInfo;
import org.impalaframework.plugin.monitor.PluginModificationListener;
import org.impalaframework.plugin.monitor.PluginMonitor;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ImpalaRootServlet extends DispatcherServlet implements PluginModificationListener {

	final Logger logger = LoggerFactory.getLogger(ImpalaRootServlet.class);

	private static final long serialVersionUID = 1L;

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	private final Lock r = rwl.readLock();

	private final Lock w = rwl.writeLock();

	private boolean initialized;

	public ImpalaRootServlet() {
		super();
	}

	// lifted straight from XmlWebApplicationContext
	protected String[] getDefaultConfigLocations() {
		if (getNamespace() != null) {
			return new String[] { WebConstants.DEFAULT_CONFIG_LOCATION_PREFIX + getNamespace() + WebConstants.DEFAULT_CONFIG_LOCATION_SUFFIX };
		}
		else {
			return new String[] { WebConstants.DEFAULT_CONFIG_LOCATION };
		}
	}

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		r.lock();
		try {
			super.doService(request, response);
		}
		finally {
			r.unlock();
		}
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() throws BeansException {
		w.lock();
		try {
			WebApplicationContext wac = createWebApplicationContext();

			onRefresh(wac);

			if (isPublishContext()) {
				// Publish the context as a servlet context attribute.
				String attrName = getServletContextAttributeName();
				getServletContext().setAttribute(attrName, wac);
				if (logger.isDebugEnabled()) {
					logger.debug("Published WebApplicationContext of servlet '" + getServletName()
							+ "' as ServletContext attribute with name [" + attrName + "]");
				}
			}

			return wac;
		}
		finally {
			w.unlock();
		}
	}

	protected WebApplicationContext createWebApplicationContext() throws BeansException {
		
		ImpalaBootstrapFactory factory = (ImpalaBootstrapFactory) getServletContext().getAttribute(
				WebConstants.IMPALA_FACTORY_PARAM);

		if (factory == null) {
			throw new RuntimeException(ImpalaBootstrapFactory.class.getSimpleName()
					+ " not set. Have you set up your Impala context loader properly? "
					+ "You need to set up a Spring context loader which will set up the parameter '"
					+ WebConstants.IMPALA_FACTORY_PARAM + "'");
		}

		PluginStateManager pluginStateManager = factory.getPluginStateManager();

		String pluginName = getServletName();
		if (!initialized) {

			// need to implement this here as the plugin is being added for the
			// first time
			ParentSpec existing = pluginStateManager.getParentSpec();
			ParentSpec newSpec = pluginStateManager.cloneParentSpec();
			newPluginSpec(pluginName, newSpec);

			PluginModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry()
					.getPluginModificationCalculator(ModificationCalculationType.STRICT);
			PluginTransitionSet transitions = calculator.getTransitions(existing, newSpec);

			pluginStateManager.processTransitions(transitions);

		}

		ApplicationContext context = pluginStateManager.getPlugins().get(pluginName);

		if (factory.containsBean("scheduledPluginMonitor") && !initialized) {
			logger.info("Registering " + getServletName() + " for plugin modifications");
			PluginMonitor pluginMonitor = (PluginMonitor) factory.getBean("scheduledPluginMonitor");
			pluginMonitor.addModificationListener(this);
		}

		this.initialized = true;

		return (WebApplicationContext) context;
	}

	protected PluginSpec newPluginSpec(String pluginName, ParentSpec parentSpec) {
		return new WebRootPluginSpec(parentSpec, pluginName, getSpringConfigLocations());
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

	public void pluginModified(PluginModificationEvent event) {
		List<PluginModificationInfo> modifiedPlugins = event.getModifiedPlugins();
		for (PluginModificationInfo info : modifiedPlugins) {
			if (getServletName().equals(info.getPluginName())) {
				try {
					if (logger.isDebugEnabled())
						logger.debug("Re-initialising plugin {}", info.getPluginName());
					initServletBean();
				}
				catch (Exception e) {
					logger.error("Unable to reload plugin {}", info.getPluginName(), e);
				}
				return;
			}
		}
	}

}
