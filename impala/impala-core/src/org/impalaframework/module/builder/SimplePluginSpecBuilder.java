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

package org.impalaframework.module.builder;

import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;
import org.impalaframework.module.spec.SimpleRootModuleDefinition;
import org.impalaframework.module.spec.SimpleModuleDefinition;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimplePluginSpecBuilder implements ModuleDefinitionSource {

	private RootModuleDefinition parent;

	public SimplePluginSpecBuilder(String[] parentContextLocations, String[] pluginNames) {
		super();
		this.parent = new SimpleRootModuleDefinition(parentContextLocations);
		setPluginNames(this.parent, pluginNames);
	}
	
	public SimplePluginSpecBuilder(String parentContextLocation, String[] pluginNames) {
		this(new String[] { parentContextLocation }, pluginNames);
	}

	public SimplePluginSpecBuilder(String[] pluginNames) {
		super();
		this.parent = new SimpleRootModuleDefinition(new String[] { "applicationContext.xml" });
		setPluginNames(this.parent, pluginNames);
	}

	public RootModuleDefinition getPluginSpec() {
		return parent;
	}

	private void setPluginNames(ModuleDefinition parent, String[] pluginNames) {
		Assert.notNull(pluginNames);
		
		ModuleDefinition[] plugins = new ModuleDefinition[pluginNames.length];
		for (int i = 0; i < pluginNames.length; i++) {
			Assert.notNull(pluginNames[i]);
			plugins[i] = new SimpleModuleDefinition(parent, pluginNames[i]);
		}
	}

}
