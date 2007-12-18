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

package org.impalaframework.module.transition;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.ModuleTransition;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class DefaultPluginStateManager implements PluginStateManager {

	final Logger logger = LoggerFactory.getLogger(DefaultPluginStateManager.class);

	private RootModuleDefinition rootModuleDefinition;

	private TransitionProcessorRegistry transitionProcessorRegistry;

	private Map<String, ConfigurableApplicationContext> plugins = new HashMap<String, ConfigurableApplicationContext>();

	public DefaultPluginStateManager() {
		super();
	}

	public void processTransitions(ModuleTransitionSet pluginTransitions) {
		
		try {
			Assert.notNull(transitionProcessorRegistry, TransitionProcessorRegistry.class.getSimpleName() + " cannot be null");

			Collection<? extends ModuleStateChange> changes = pluginTransitions.getPluginTransitions();

			for (ModuleStateChange change : changes) {
				ModuleTransition transition = change.getTransition();
				ModuleDefinition moduleDefinition = change.getPluginSpec();

				TransitionProcessor transitionProcessor = transitionProcessorRegistry.getTransitionProcessor(transition);
				transitionProcessor.process(this, rootModuleDefinition, pluginTransitions.getNewSpec(), moduleDefinition);
			}
		} finally {
			rootModuleDefinition = pluginTransitions.getNewSpec();
		}
	}

	public ConfigurableApplicationContext getParentContext() {
		return plugins.get(RootModuleDefinition.NAME);
	}

	public ConfigurableApplicationContext getPlugin(String name) {
		return plugins.get(name);
	}

	public RootModuleDefinition getParentSpec() {
		return rootModuleDefinition;
	}

	public RootModuleDefinition cloneParentSpec() {
		return (RootModuleDefinition) SerializationUtils.clone(rootModuleDefinition);
	}

	public boolean hasPlugin(String plugin) {
		return (rootModuleDefinition.findPlugin(plugin, true) != null);
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
	
	public RootModuleDefinition getModuleDefintion() {
		return getParentSpec();
	}

	/* ************************* protected methods ************************* */

	protected void setParentSpec(RootModuleDefinition rootModuleDefinition) {
		this.rootModuleDefinition = rootModuleDefinition;
	}

	/* ******************** injected setters ******************** */
	
	public void setTransitionProcessorRegistry(TransitionProcessorRegistry transitionProcessorRegistry) {
		this.transitionProcessorRegistry = transitionProcessorRegistry;
	}

}
