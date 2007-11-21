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

package org.impalaframework.plugin.transition;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.modification.PluginStateChange;
import org.impalaframework.plugin.modification.PluginTransition;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class DefaultPluginStateManager implements PluginStateManager {

	final Logger logger = LoggerFactory.getLogger(DefaultPluginStateManager.class);

	private ParentSpec parentSpec;

	private ApplicationContextLoader contextLoader;

	private TransitionProcessorRegistry transitionProcessorRegistry;

	private Map<String, ConfigurableApplicationContext> plugins = new HashMap<String, ConfigurableApplicationContext>();

	public DefaultPluginStateManager() {
		super();
	}

	public void processTransitions(PluginTransitionSet pluginTransitions) {
		
		try {
			Assert.notNull(transitionProcessorRegistry, TransitionProcessorRegistry.class.getSimpleName() + " cannot be null");

			Collection<? extends PluginStateChange> changes = pluginTransitions.getPluginTransitions();

			for (PluginStateChange change : changes) {
				PluginTransition transition = change.getTransition();
				PluginSpec pluginSpec = change.getPluginSpec();

				TransitionProcessor transitionProcessor = transitionProcessorRegistry.getTransitionProcessor(transition);
				transitionProcessor.process(this, parentSpec, pluginTransitions.getNewSpec(), pluginSpec);
			}
		} finally {
			parentSpec = pluginTransitions.getNewSpec();
		}
	}

	public ConfigurableApplicationContext getParentContext() {
		return plugins.get(ParentSpec.NAME);
	}

	public ConfigurableApplicationContext getPlugin(String name) {
		return plugins.get(name);
	}

	public ApplicationContextLoader getContextLoader() {
		return contextLoader;
	}

	public ParentSpec getParentSpec() {
		return parentSpec;
	}

	public ParentSpec cloneParentSpec() {
		return (ParentSpec) SerializationUtils.clone(parentSpec);
	}

	public boolean hasPlugin(String plugin) {
		return (parentSpec.findPlugin(plugin, true) != null);
	}

	public boolean hasParentContext() {
		return getParentSpec() != null;
	}

	public Map<String, ConfigurableApplicationContext> getPlugins() {
		return Collections.unmodifiableMap(plugins);
	}
	
	public void putPlugin(String name, ConfigurableApplicationContext context) {
		plugins.put(name, context);
	}

	public ConfigurableApplicationContext removePlugin(String name) {
		return plugins.remove(name);
	}

	/* ************************* package level methods ************************* */

	void setParentSpec(ParentSpec parentSpec) {
		this.parentSpec = parentSpec;
	}

	/* ******************** injected setters ******************** */

	public void setApplicationContextLoader(ApplicationContextLoader contextLoader) {
		this.contextLoader = contextLoader;
	}

	public void setTransitionProcessorRegistry(TransitionProcessorRegistry transitionProcessorRegistry) {
		this.transitionProcessorRegistry = transitionProcessorRegistry;
	}

}
