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

import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpec;
import org.impalaframework.module.spec.PluginSpecProvider;
import org.impalaframework.module.spec.SimpleParentSpec;
import org.impalaframework.module.spec.SimplePluginSpec;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimplePluginSpecBuilder implements PluginSpecProvider {

	private ParentSpec parent;

	public SimplePluginSpecBuilder(String[] parentContextLocations, String[] pluginNames) {
		super();
		this.parent = new SimpleParentSpec(parentContextLocations);
		setPluginNames(this.parent, pluginNames);
	}
	
	public SimplePluginSpecBuilder(String parentContextLocation, String[] pluginNames) {
		this(new String[] { parentContextLocation }, pluginNames);
	}

	public SimplePluginSpecBuilder(String[] pluginNames) {
		super();
		this.parent = new SimpleParentSpec(new String[] { "applicationContext.xml" });
		setPluginNames(this.parent, pluginNames);
	}

	public ParentSpec getPluginSpec() {
		return parent;
	}

	private void setPluginNames(PluginSpec parent, String[] pluginNames) {
		Assert.notNull(pluginNames);
		
		PluginSpec[] plugins = new PluginSpec[pluginNames.length];
		for (int i = 0; i < pluginNames.length; i++) {
			Assert.notNull(pluginNames[i]);
			plugins[i] = new SimplePluginSpec(parent, pluginNames[i]);
		}
	}

}
