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

package org.impalaframework.module.spec;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimplePluginSpec implements PluginSpec {

	private static final long serialVersionUID = 1L;

	private String name;

	private ChildSpecContainer childContainer;

	private PluginSpec parent;

	public SimplePluginSpec(String name) {
		super();
		Assert.notNull(name);
		this.name = name;
		this.childContainer = new ChildSpecContainerImpl();
	}

	public SimplePluginSpec(PluginSpec parent, String name) {
		super();
		Assert.notNull(name);
		Assert.notNull(parent);
		this.name = name;
		this.childContainer = new ChildSpecContainerImpl();
		this.parent = parent;
		this.parent.add(this);
	}
	
	public PluginSpec findPlugin(String pluginName, boolean exactMatch) {
		return PluginSpecUtils.findPlugin(pluginName, this, exactMatch);
	}
	
	public List<String> getContextLocations() {
		return Collections.singletonList(this.name + "-context.xml");
	}

	public String getName() {
		return name;
	}

	public PluginSpec getParent() {
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
	
	public void setParent(PluginSpec parent) {
		this.parent = parent;
	}

	public String getType() {
		return PluginTypes.APPLICATION;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SimplePluginSpec other = (SimplePluginSpec) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

}
