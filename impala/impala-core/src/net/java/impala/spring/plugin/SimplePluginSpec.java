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

public class SimplePluginSpec implements PluginSpec {
	
	private String[] parentContextLocations;

	private String[] pluginNames;

	public SimplePluginSpec(String[] parentContextLocations, String[] pluginNames) {
		super();
		this.parentContextLocations = parentContextLocations;
		this.pluginNames = pluginNames;
	}

	public SimplePluginSpec(String parentContextLocation, String[] pluginNames) {
		super();
		this.parentContextLocations = new String[] { parentContextLocation };
		this.pluginNames = pluginNames;
	}

	public SimplePluginSpec(String[] pluginNames) {
		super();
		this.parentContextLocations = new String[] { "applicationContext.xml" };
		this.pluginNames = pluginNames;
	}

	public String[] getParentContextLocations() {
		return parentContextLocations;
	}

	public String[] getPluginNames() {
		return pluginNames;
	}

}
