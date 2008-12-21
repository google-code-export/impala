/*
 * Copyright 2007-2008 the original author or authors.
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.impalaframework.module.ChildModuleContainer;
import org.impalaframework.module.ModuleDefinition;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class ChildModuleContainerImpl implements ChildModuleContainer {
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, ModuleDefinition> definitions = new LinkedHashMap<String, ModuleDefinition>();

	public ChildModuleContainerImpl(ModuleDefinition[] definitions) {
		super();
		Assert.notNull(definitions);
		for (ModuleDefinition definition : definitions) {
			add(definition);
		}
	}
	
	public ChildModuleContainerImpl() {
	}

	public Collection<String> getModuleNames() {
		return Collections.unmodifiableCollection(definitions.keySet());
	}

	public ModuleDefinition getModule(String moduleName) {
		return definitions.get(moduleName);
	}

	public boolean hasDefinition(String moduleName) {
		return getModule(moduleName) != null;
	}

	public Collection<ModuleDefinition> getChildDefinitions() {
		return Collections.unmodifiableCollection(definitions.values());
	}

	public void add(ModuleDefinition moduleDefinition) {
		final String name = moduleDefinition.getName();
		this.definitions.put(name, moduleDefinition);
	}

	public ModuleDefinition remove(String moduleName) {
		return definitions.remove(moduleName);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((definitions == null) ? 0 : definitions.hashCode());
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
		if (definitions == null) {
			if (other.definitions != null)
				return false;
		}
		else if (!definitions.equals(other.definitions))
			return false;
		return true;
	}

	
	
	
}
