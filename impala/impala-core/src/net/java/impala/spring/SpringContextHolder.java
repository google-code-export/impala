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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.java.impala.spring.plugin.ApplicationContextSet;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.spring.util.ApplicationContextLoader;
import net.java.impala.spring.util.DefaultApplicationContextLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class SpringContextHolder {

	private static final Log log = LogFactory.getLog(SpringContextHolder.class);

	private ApplicationContextLoader contextLoader;
	
	private SpringContextSpec pluginSpec;

	private ConfigurableApplicationContext context;

	private Map<String, ConfigurableApplicationContext> plugins = new HashMap<String, ConfigurableApplicationContext>();

	public SpringContextHolder(ApplicationContextLoader contextLoader) {
		super();
		Assert.notNull(contextLoader, DefaultApplicationContextLoader.class.getSimpleName() + " cannot be null");
		this.contextLoader = contextLoader;
	}

	public void shutParentConext() {
		Set<String> pluginKeys = plugins.keySet();
		attemptClosePlugins(pluginKeys);
		attemptCloseParent();
	}
	
	public boolean loadParentContext(ClassLoader classLoader, SpringContextSpec spec) {
		this.pluginSpec = spec;
		return loadParentContext(classLoader);
	}

	private boolean loadParentContext(ClassLoader classLoader) {
		
		boolean reload = false;
		
		try {
			ApplicationContextSet contextSet = contextLoader.loadParentContext(pluginSpec, classLoader);
			Map<String, ConfigurableApplicationContext> pluginMap = contextSet.getPluginContext();
			Set<String> pluginKeys = pluginMap.keySet();
			
			attemptCloseParent();
			attemptClosePlugins(pluginKeys);
			
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
			
			try {
				ConfigurableApplicationContext pluginContext = contextLoader.addApplicationPlugin(this.context, plugin);
				plugins.put(plugin.getName(), pluginContext);
				return true;
			}
			catch (Exception e) {
				log.error("Exception attempting to load plugin " + plugin + ": " + e.getMessage(), e);
				return false;
			}
		}
		return false;
	}

	public void removePlugin(String remove) {
		ConfigurableApplicationContext toRemove = plugins.remove(remove);
		if (toRemove != null)
			toRemove.close();
	}
	
	private void attemptClosePlugins(Set<String> loadedPluginNames) {
		for (String plugin : loadedPluginNames) {
			
			if (plugins.containsKey(plugin)) {
				ConfigurableApplicationContext existing = plugins.get(plugin);
				try {
					existing.close();
				}
				catch (RuntimeException e) {
					log.error("Exception attempting to close plugin " + plugin + ": " + e.getMessage(), e);
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

	public boolean hasPlugin(String pluginName) {
		return plugins.get(pluginName) != null;
	}

	public PluginSpec getPlugin(String pluginName) {
		return pluginSpec.getPlugin(pluginName);
	}
	
	public ApplicationContextLoader getContextLoader() {
		return contextLoader;
	}
	
	protected Map<String, ConfigurableApplicationContext> getPlugins() {
		return plugins;
	}

	public boolean hasParentContext() {
		return (context != null);
	}

}
