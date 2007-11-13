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

package org.impalaframework.plugin.spec.transition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ApplicationContextSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.modification.PluginStateChange;
import org.impalaframework.plugin.spec.modification.PluginTransition;
import org.impalaframework.plugin.spec.modification.PluginTransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class PluginStateManager {

	final Logger logger = LoggerFactory.getLogger(PluginStateManager.class);

	private ApplicationContextLoader contextLoader;

	private Map<String, ConfigurableApplicationContext> plugins = new HashMap<String, ConfigurableApplicationContext>();

	public PluginStateManager() {
		super();
	}

	public void processTransitions(PluginTransitionSet pluginTransitions) {

		// FIXME more tests
		Assert.notNull(contextLoader, ApplicationContextLoader.class.getSimpleName() + " cannot be null");

		Collection<? extends PluginStateChange> changes = pluginTransitions.getPluginTransitions();

		for (PluginStateChange change : changes) {
			PluginTransition transition = change.getTransition();
			PluginSpec pluginSpec = change.getPluginSpec();

			if (PluginTransition.LOADED_TO_UNLOADED.equals(transition)) {
				unload(pluginSpec);
			}
			else if (PluginTransition.UNLOADED_TO_LOADED.equals(transition)) {
				load(pluginSpec);
			}
		}
	}

	public ConfigurableApplicationContext getParentContext() {
		return plugins.get(ParentSpec.NAME);
	}

	public ConfigurableApplicationContext getPlugin(String name) {
		return plugins.get(name);
	}

	void unload(PluginSpec pluginSpec) {

		ConfigurableApplicationContext appContext = plugins.remove(pluginSpec.getName());
		if (appContext != null) {
			appContext.close();
		}

	}

	void load(PluginSpec plugin) {

		if (plugins.get(plugin.getName()) == null) {
			ConfigurableApplicationContext parent = null;
			PluginSpec parentSpec = plugin.getParent();
			if (parentSpec != null) {
				parent = plugins.get(parentSpec.getName());
			}

			ApplicationContextSet appSet = new ApplicationContextSet();
			plugins.put(plugin.getName(), contextLoader.loadContext(appSet, plugin, parent));
		} else {
			logger.warn("Attemtp to load plugin " + plugin.getName() + " which was already loaded. Suggest calling unload first.");
		}

	}

	public void setApplicationContextLoader(ApplicationContextLoader contextLoader) {
		this.contextLoader = contextLoader;
	}

}
