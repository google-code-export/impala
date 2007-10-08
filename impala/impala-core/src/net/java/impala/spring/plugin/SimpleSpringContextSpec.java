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

import java.util.Collection;

import org.springframework.util.Assert;

public class SimpleSpringContextSpec implements SpringContextSpec {

	private ParentSpec parent;

	private ChildSpecContainer childContainer;

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

	public Collection<String> getPluginNames() {
		return childContainer.getPluginNames();
	}
	
	public PluginSpec getPlugin(String pluginName) {
		return childContainer.getPlugin(pluginName);
	}
	
	public Collection<PluginSpec> getPlugins() {
		return childContainer.getPlugins();
	}

	public boolean hasPlugin(String pluginName) {
		return getPlugin(pluginName) != null;
	}

	public void add(PluginSpec pluginSpec) {
		childContainer.add(pluginSpec);
	}

	public PluginSpec remove(String pluginName) {
		return childContainer.remove(pluginName);
	}

	private void setPluginNames(String[] pluginNames) {
		Assert.notNull(pluginNames);
		
		PluginSpec[] plugins = new PluginSpec[pluginNames.length];
		for (int i = 0; i < pluginNames.length; i++) {
			Assert.notNull(pluginNames[i]);
			plugins[i] = new SimplePluginSpec(pluginNames[i]);
		}
		this.childContainer = new ChildSpecContainerImpl(plugins);
	}

}
