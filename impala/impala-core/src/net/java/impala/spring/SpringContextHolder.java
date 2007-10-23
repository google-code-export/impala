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

package net.java.impala.spring;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.java.impala.spring.plugin.ApplicationContextSet;
import net.java.impala.spring.plugin.ParentSpec;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.spring.util.ApplicationContextLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SpringContextHolder {

	private static final Log log = LogFactory.getLog(SpringContextHolder.class);

	private ApplicationContextLoader contextLoader;

	private SpringContextSpec pluginSpec;

	private ConfigurableApplicationContext context;

	private Map<String, ConfigurableApplicationContext> plugins = new HashMap<String, ConfigurableApplicationContext>();

	public SpringContextHolder(ApplicationContextLoader contextLoader) {
		super();
		Assert.notNull(contextLoader, ApplicationContextLoader.class.getSimpleName() + " cannot be null");
		this.contextLoader = contextLoader;
	}

	public void shutParentConext() {
		Set<String> pluginKeys = plugins.keySet();
		attemptClosePlugins(pluginKeys);
		attemptCloseParent();
	}

	public boolean loadParentContext(SpringContextSpec spec) {
		setSpringContextSpec(spec);
		return loadParentContext();
	}

	public void setSpringContextSpec(SpringContextSpec spec) {
		this.pluginSpec = spec;
	}

	public boolean loadParentContext() {

		boolean reload = false;

		try {
			ApplicationContextSet contextSet = new ApplicationContextSet();
			contextLoader.addApplicationPlugin(contextSet, pluginSpec.getParentSpec(), null);

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
			log.error("Exception attempting to load parent context: " + e.getMessage(), e);
		}
		return reload;
	}

	public boolean addPlugin(PluginSpec plugin) {
		if (!plugins.containsKey(plugin)) {

			ConfigurableApplicationContext parentContext = this.context;

			final PluginSpec parentPlugin = plugin.getParent();
			if (parentPlugin != null) {
				final ConfigurableApplicationContext pluginParent = plugins.get(parentPlugin.getName());
				if (pluginParent != null)
					parentContext = pluginParent;
			}

			try {
				final ApplicationContextSet appSet = new ApplicationContextSet();
				contextLoader.addApplicationPlugin(appSet, plugin, parentContext);

				// transfer any loaded plugins to plugins map
				plugins.putAll(appSet.getPluginContext());
				return true;
			}
			catch (Exception e) {
				log.error("Exception attempting to load plugin " + plugin + ": " + e.getMessage(), e);
				return false;
			}
		}
		return false;
	}

	public void removePlugin(PluginSpec remove) {
		ConfigurableApplicationContext toRemove = plugins.remove(remove.getName());
		if (toRemove != null)
			toRemove.close();
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
						log.error("Exception attempting to close plugin " + plugin + ": " + e.getMessage(), e);
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
				log.error("Failed attempting to close parent plugin");
			}
			context = null;
		}
	}

	public ApplicationContext getContext() {
		return context;
	}

	public ParentSpec getParent() {
		if (pluginSpec == null)
			return null;
		return pluginSpec.getParentSpec();
	}

	public PluginSpec getPlugin(String pluginName) {
		final ParentSpec parent = getParent();
		if (parent != null) {
			return findPlugin(pluginName, parent, true);
		}
		return null;
	}

	public PluginSpec findPluginLike(String pluginLikeName) {
		final ParentSpec parent = getParent();
		if (parent != null) {
			return findPlugin(pluginLikeName, parent, false);
		}
		return null;
	}

	private PluginSpec findPlugin(String pluginName, final PluginSpec pluginSpec, boolean exactMatch) {

		// FIXME can we move this to childSpecContainer
		if (exactMatch) {
			if (pluginName.equals(pluginSpec.getName()))
				return pluginSpec;
		}
		else {
			if (pluginSpec.getName().contains(pluginName))
				return pluginSpec;
		}

		final Collection<PluginSpec> childPlugins = pluginSpec.getPlugins();
		for (PluginSpec childSpec : childPlugins) {
			final PluginSpec findPlugin = findPlugin(pluginName, childSpec, exactMatch);
			if (findPlugin != null) {
				return findPlugin;
			}
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

	protected SpringContextSpec getPluginSpec() {
		return pluginSpec;
	}

	public boolean hasParentContext() {
		return (context != null);
	}

}
