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

public class SimpleSpringContextSpec implements SpringContextSpec {

	private ParentSpec parent;

	private PluginSpec[] plugins;

	public SimpleSpringContextSpec(String[] parentContextLocations, String[] pluginNames) {
		super();
		this.parent = new SimpleParentSpec(parentContextLocations);
		setPluginNames(pluginNames);
	}
	
	public SimpleSpringContextSpec(String parentContextLocation, String[] pluginNames) {
		this(new String[] { parentContextLocation }, pluginNames);
	}

	public SimpleSpringContextSpec(String[] pluginNames) {
		super();
		this.parent = new SimpleParentSpec(new String[] { "applicationContext.xml" });
		setPluginNames(pluginNames);
	}

	public ParentSpec getParentSpec() {
		return parent;
	}

	public String[] getParentContextLocations() {
		return getParentSpec().getParentContextLocations();
	}

	public String[] getPluginNames() {
		String[] pluginNames = new String[plugins.length];
		for (int i = 0; i < pluginNames.length; i++) {
			pluginNames[i] = plugins[i].getName();
		}
		return pluginNames;
	}
	
	public PluginSpec getPlugin(String pluginName) {
		for (PluginSpec plugin : plugins) {
			if (plugin.getName().equals(pluginName)) {
				return plugin;
			}
		}
		return null;
	}

	public boolean hasPlugin(String pluginName) {
		return getPlugin(pluginName) != null;
	}

	public PluginSpec[] getPlugins() {
		return plugins;
	}

	private void setPluginNames(String[] pluginNames) {
		Assert.notNull(pluginNames);
		
		PluginSpec[] plugins = new PluginSpec[pluginNames.length];
		for (int i = 0; i < pluginNames.length; i++) {
			Assert.notNull(pluginNames[i]);
			plugins[i] = new SimplePluginSpec(pluginNames[i]);
		}
		this.plugins = plugins;
	}


}
