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

package net.java.impala.spring.plugin;

import org.springframework.util.Assert;

public class SimplePluginSpec implements SpringContextSpec {

	private String[] parentContextLocations;

	private Plugin[] plugins;

	public SimplePluginSpec(String[] parentContextLocations, String[] pluginNames) {
		super();
		Assert.notNull(parentContextLocations);
		for (int i = 0; i < parentContextLocations.length; i++) {
			Assert.notNull(parentContextLocations[i]);
		}

		this.parentContextLocations = parentContextLocations;
		setPluginNames(pluginNames);
	}

	public SimplePluginSpec(String parentContextLocation, String[] pluginNames) {
		this(new String[] { parentContextLocation }, pluginNames);
	}

	public SimplePluginSpec(String[] pluginNames) {
		super();
		this.parentContextLocations = new String[] { "applicationContext.xml" };
		setPluginNames(pluginNames);
	}

	public String[] getParentContextLocations() {
		return parentContextLocations;
	}

	public String[] getPluginNames() {
		String[] pluginNames = new String[plugins.length];
		for (int i = 0; i < pluginNames.length; i++) {
			pluginNames[i] = plugins[i].getName();
		}
		return pluginNames;
	}
	
	public Plugin getPlugin(String pluginName) {
		for (Plugin plugin : plugins) {
			if (plugin.getName().equals(pluginName)) {
				return plugin;
			}
		}
		return null;
	}

	public boolean hasPlugin(String pluginName) {
		return getPlugin(pluginName) != null;
	}

	public Plugin[] getPlugins() {
		return plugins;
	}

	private void setPluginNames(String[] pluginNames) {
		Assert.notNull(pluginNames);
		
		Plugin[] plugins = new Plugin[pluginNames.length];
		for (int i = 0; i < pluginNames.length; i++) {
			Assert.notNull(pluginNames[i]);
			plugins[i] = new SimplePlugin(pluginNames[i]);
		}
		this.plugins = plugins;
	}

}
