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

import org.apache.commons.lang.SerializationUtils;
import org.impalaframework.plugin.monitor.PluginModificationEvent;
import org.impalaframework.plugin.monitor.PluginModificationInfo;
import org.impalaframework.plugin.monitor.PluginModificationListener;
import org.impalaframework.plugin.monitor.PluginMonitor;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.modification.PluginModificationCalculator;
import org.impalaframework.plugin.spec.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.transition.DefaultPluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class RegistryBasedImpalaServlet extends DispatcherServlet implements PluginModificationListener {
	
	final Logger logger = LoggerFactory.getLogger(RegistryBasedImpalaServlet.class);

	/** Default config location for the root context */
	public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";

	/** Default prefix for building a config location for a namespace */
	public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";

	/** Default suffix for building a config location for a namespace */
	public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

	private static final long serialVersionUID = 1L;

	private PluginMonitor pluginMonitor;
	
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();

	public RegistryBasedImpalaServlet() {
		super();
	}

	// lifted straight from XmlWebApplicationContext
	protected String[] getDefaultConfigLocations() {
		if (getNamespace() != null) {
			return new String[] { DEFAULT_CONFIG_LOCATION_PREFIX + getNamespace() + DEFAULT_CONFIG_LOCATION_SUFFIX };
		}
		else {
			return new String[] { DEFAULT_CONFIG_LOCATION };
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

		//FIXME attempt to get plugin corresponding with servlet name from 
		//holder. If not present, then use the web root context
		
		DefaultPluginStateManager holder = (DefaultPluginStateManager) getServletContext().getAttribute(
				RegistryBasedImpalaContextLoader.CONTEXT_HOLDER_PARAM);

		if (holder == null) {
			throw new RuntimeException(
					"WebDynamicContextHolder not set. Have you set up your Impala context loader properly?");
		}

		ParentSpec existing = holder.getParentSpec();
		ParentSpec newSpec = (ParentSpec) SerializationUtils.clone(existing);
		PluginSpec plugin = new WebServletSpec(newSpec, getServletName(), getSpringConfigLocations());
		
		PluginModificationCalculator calculator = new PluginModificationCalculator();
		PluginTransitionSet transitions = calculator.getTransitions(existing, newSpec);
		
		holder.processTransitions(transitions);

		if (pluginMonitor == null) {
			pluginMonitor = holder.getContextLoader().getPluginMonitor();
			pluginMonitor.addModificationListener(this);
		}

		ApplicationContext context = holder.getPlugins().get(plugin.getName());
		return (WebApplicationContext) context;

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
