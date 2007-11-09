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

package org.impalaframework.spring;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ApplicationContextSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class DefaultSpringContextHolder implements SpringContextHolder {
	
	final Logger logger = LoggerFactory.getLogger(DefaultSpringContextHolder.class);

	private ApplicationContextLoader contextLoader;

	private ParentSpec pluginSpec;

	private ConfigurableApplicationContext context;

	private Map<String, ConfigurableApplicationContext> plugins = new HashMap<String, ConfigurableApplicationContext>();

	public DefaultSpringContextHolder(ApplicationContextLoader contextLoader) {
		super();
		Assert.notNull(contextLoader, ApplicationContextLoader.class.getSimpleName() + " cannot be null");
		this.contextLoader = contextLoader;
	}

	public void shutParentContext() {
		Set<String> pluginKeys = plugins.keySet();
		attemptClosePlugins(pluginKeys);
		attemptCloseParent();
	}

	public boolean loadParentContext(ParentSpec spec) {
		setSpringContextSpec(spec);
		return loadParentContext();
	}

	public void setSpringContextSpec(ParentSpec spec) {
		this.pluginSpec = spec;
	}

	public boolean loadParentContext() {

		boolean reload = false;

		try {
			ApplicationContextSet contextSet = new ApplicationContextSet(this.plugins);
			contextLoader.addApplicationPlugin(contextSet, pluginSpec, null);

			Map<String, ConfigurableApplicationContext> pluginMap = contextSet.getPluginContext();
			Set<String> pluginKeys = pluginMap.keySet();

			attemptClosePlugins(pluginKeys);
			attemptCloseParent();

			for (String plugin : pluginKeys) {
				plugins.put(plugin, pluginMap.get(plugin));
			}
			this.context = contextSet.getContext();

			reload = true;
		}
		catch (RuntimeException e) {
			logger.error("Exception attempting to load parent context: {}", e.getMessage(), e);
		}
		return reload;
	}

	public boolean addPlugin(PluginSpec plugin) {
		
		if (!plugins.containsKey(plugin)) {

			ConfigurableApplicationContext parentContext = this.context;

			try {

				final PluginSpec parentPlugin = plugin.getParent();
				
				if (parentPlugin != null) {
					final ConfigurableApplicationContext pluginParent = plugins.get(parentPlugin.getName());
					if (pluginParent != null)
						parentContext = pluginParent;

					PluginSpec pluginToAdd = (PluginSpec) SerializationUtils.clone(plugin);

					PluginSpec foundParent = findPluginReference(parentPlugin);

					if (foundParent == null) {
						throw new IllegalStateException("Unable to find reference to parent plugin "
								+ parentPlugin.getName());
					}

					pluginToAdd.setParent(foundParent);
					foundParent.add(pluginToAdd);
				}

				final ApplicationContextSet appSet = new ApplicationContextSet(this.plugins);
				contextLoader.addApplicationPlugin(appSet, plugin, parentContext);

				// transfer any loaded plugins to plugins map
				plugins.putAll(appSet.getPluginContext());
				return true;
			}
			catch (Exception e) {
				logger.error("Exception attempting to load plugin " + plugin + ": " + e.getMessage(), e);
				return false;
			}
		}
		return false;
		
	}

	public void removePlugin(PluginSpec pluginSpec, boolean remove) {
		ConfigurableApplicationContext toRemove = plugins.remove(pluginSpec.getName());
		if (toRemove != null)
			toRemove.close();
	
		if (remove) {
			final PluginSpec parentPlugin = pluginSpec.getParent();
			if (parentPlugin != null) {
				PluginSpec foundParent = findPluginReference(parentPlugin);
				foundParent.remove(pluginSpec.getName());
			}
		}
	}

	PluginSpec findPluginReference(PluginSpec pluginToFind) {
		String parentName = pluginToFind.getName();
		return pluginSpec.findPlugin(parentName, true);
	}

	private void attemptClosePlugins(Set<String> loadedPluginNames) {
		for (String plugin : loadedPluginNames) {

			if (plugins.containsKey(plugin)) {
				ConfigurableApplicationContext existing = plugins.get(plugin);
				if (!plugin.equals(ParentSpec.NAME)) {
					try {
						existing.close();
					}
					catch (RuntimeException e) {
						logger.error("Exception attempting to close plugin " + plugin + ": " + e.getMessage(), e);
					}
				}
			}
		}
		plugins.clear();
	}

	private void attemptCloseParent() {
		if (context != null) {

			try {
				context.close();
			}
			catch (RuntimeException e) {
				logger.error("Failed attempting to close parent plugin");
			}
			context = null;
		}
	}

	public ApplicationContext getContext() {
		return context;
	}

	public ParentSpec getParent() {
		return pluginSpec;
	}

	public PluginSpec getPlugin(String pluginName) {
		final ParentSpec parent = getParent();
		if (parent != null) {
			return parent.findPlugin(pluginName, true);
		}
		return null;
	}

	public PluginSpec findPluginLike(String pluginLikeName) {
		final ParentSpec parent = getParent();
		if (parent != null) {
			return parent.findPlugin(pluginLikeName, false);
		}
		return null;
	}

	public boolean hasPlugin(String pluginName) {
		return plugins.get(pluginName) != null;
	}

	public ApplicationContextLoader getContextLoader() {
		return contextLoader;
	}

	public Map<String, ConfigurableApplicationContext> getPlugins() {
		return Collections.unmodifiableMap(plugins);
	}

	protected ParentSpec getPluginSpec() {
		return pluginSpec;
	}

	public boolean hasParentContext() {
		return (context != null);
	}

}
