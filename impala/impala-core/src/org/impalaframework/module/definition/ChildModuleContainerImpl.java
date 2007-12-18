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

package org.impalaframework.module.definition;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class ChildModuleContainerImpl implements ChildModuleContainer {
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, ModuleDefinition> plugins = new LinkedHashMap<String, ModuleDefinition>();

	public ChildModuleContainerImpl(ModuleDefinition[] plugins) {
		super();
		Assert.notNull(plugins);
		for (ModuleDefinition spec : plugins) {
			add(spec);
		}
	}
	
	public ChildModuleContainerImpl() {
	}

	public Collection<String> getPluginNames() {
		return plugins.keySet();
	}

	public ModuleDefinition getPlugin(String pluginName) {
		return plugins.get(pluginName);
	}

	public boolean hasPlugin(String pluginName) {
		return getPlugin(pluginName) != null;
	}

	public Collection<ModuleDefinition> getPlugins() {
		return plugins.values();
	}

	public void add(ModuleDefinition moduleDefinition) {
		final String name = moduleDefinition.getName();
		this.plugins.put(name, moduleDefinition);
	}

	public ModuleDefinition remove(String pluginName) {
		return plugins.remove(pluginName);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((plugins == null) ? 0 : plugins.hashCode());
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
		final ChildModuleContainerImpl other = (ChildModuleContainerImpl) obj;
		if (plugins == null) {
			if (other.plugins != null)
				return false;
		}
		else if (!plugins.equals(other.plugins))
			return false;
		return true;
	}

	
	
	
}
