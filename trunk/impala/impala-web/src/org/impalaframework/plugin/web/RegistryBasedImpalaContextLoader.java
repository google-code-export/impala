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

import javax.servlet.ServletContext;

import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.BeansetApplicationPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.loader.RegistryBasedApplicationContextLoader;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransition;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.monitor.ScheduledPluginMonitor;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.transition.DefaultPluginStateManager;
import org.impalaframework.plugin.transition.LoadTransitionProcessor;
import org.impalaframework.plugin.transition.TransitionProcessorRegistry;
import org.impalaframework.plugin.transition.UnloadTransitionProcessor;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class RegistryBasedImpalaContextLoader extends ContextLoader {

	public static final String PLUGIN_NAMES_PARAM = "pluginNames";

	public static final String CONTEXT_HOLDER_PARAM = WebApplicationContext.class.getName() + ".CONTEXT_HOLDER";

	public static final String WEBAPP_LOCATION_PARAM = "webappConfigLocation";

	private boolean autoreload = true;	
	
	@Override
	protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent)
			throws BeansException {		
		
		ClassLocationResolver classLocationResolver = newClassLocationResolver();

		PluginLoaderRegistry registry = newRegistry(servletContext, classLocationResolver);

		RegistryBasedApplicationContextLoader loader = new RegistryBasedApplicationContextLoader(registry);
		
		if (autoreload ) {
			ScheduledPluginMonitor monitor = new ScheduledPluginMonitor();
			monitor.addModificationListener(new WebPluginModificationListener(servletContext));
			loader.setPluginMonitor(monitor);
		}
		
		//load the parent context, which is web-independent
		ParentSpec pluginSpec = getPluginSpec(servletContext);
		
		//set up the plugin state manager
		DefaultPluginStateManager pluginStateManager = new DefaultPluginStateManager();
		
		TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
		LoadTransitionProcessor loadTransitionProcessor = new LoadTransitionProcessor(loader);
		UnloadTransitionProcessor unloadTransitionProcessor = new UnloadTransitionProcessor();
		transitionProcessors.addTransitionProcessor(PluginTransition.UNLOADED_TO_LOADED, loadTransitionProcessor);
		transitionProcessors.addTransitionProcessor(PluginTransition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
		pluginStateManager.setTransitionProcessorRegistry(transitionProcessors);
		
		//figure out the plugins to reload
		PluginModificationCalculator calculator = new PluginModificationCalculator();
		PluginTransitionSet transitions = calculator.getTransitions(null, pluginSpec);
		pluginStateManager.processTransitions(transitions);

		// add context holder to servlet context
		servletContext.setAttribute(CONTEXT_HOLDER_PARAM, pluginStateManager);
		WebApplicationContext parentContext = (WebApplicationContext) pluginStateManager.getParentContext();
		
		return parentContext;
	}
	
	protected PluginLoaderRegistry newRegistry(ServletContext servletContext, ClassLocationResolver classLocationResolver) {
		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		registry.setPluginLoader(PluginTypes.ROOT, new WebParentPluginLoader(classLocationResolver, servletContext));
		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(classLocationResolver));
		registry.setPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS, new BeansetApplicationPluginLoader(classLocationResolver));
		registry.setPluginLoader(WebPluginTypes.SERVLET, new WebPluginLoader(classLocationResolver, servletContext));
		return registry;
	}

	ParentSpec getPluginSpec(ServletContext servletContext) {

		// subclasses can override to get PluginSpec more intelligently
		String[] locations = null;
		String configLocationString = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if (configLocationString != null) {
			locations = (StringUtils.tokenizeToStringArray(configLocationString,
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS));
		}

		String pluginNameString = servletContext.getInitParameter(PLUGIN_NAMES_PARAM);
		ParentSpec parentSpec = new SimpleParentSpec(locations);
		return new SingleStringPluginSpecBuilder(parentSpec, pluginNameString).getParentSpec();
	}

	protected ClassLocationResolver newClassLocationResolver() {
		ClassLocationResolver classLocationResolver = new PropertyClassLocationResolver();
		return classLocationResolver;
	}

}
