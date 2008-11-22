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

package org.impalaframework.web.module;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.ModuleState;
import org.springframework.util.Assert;

public class WebPlaceholderModuleDefinition implements ModuleDefinition {

	private static final long serialVersionUID = 1L;

	private ModuleDefinition parent;

	private ModuleState state;

	private String name;

	public WebPlaceholderModuleDefinition(ModuleDefinition parent, String name) {
		Assert.notNull(parent);
		Assert.notNull(name);
		this.parent = parent;
		this.name = name;
		this.parent.add(this);
	}

	public void add(ModuleDefinition moduleDefinition) {
		throw new UnsupportedOperationException("Cannot add module '" + moduleDefinition.getName()
				+ "' to web placeholder module definitionSource '" + this.getName() + "', as this cannot contain other modules");
	}

	public ModuleDefinition findChildDefinition(String moduleName, boolean exactMatch) {
		return null;
	}

	public List<String> getContextLocations() {
		return Collections.emptyList();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return WebModuleTypes.WEB_PLACEHOLDER;
	}

	public ModuleDefinition getParentDefinition() {
		return this.parent;
	}

	public ModuleDefinition getModule(String moduleName) {
		return null;
	}

	public Collection<String> getModuleNames() {
		return Collections.emptyList();
	}

	public Collection<ModuleDefinition> getChildDefinitions() {
		return Collections.emptyList();
	}

	public boolean hasDefinition(String moduleName) {
		return false;
	}

	public ModuleDefinition remove(String moduleName) {
		return null;
	}

	public void setParentDefinition(ModuleDefinition parent) {
		this.parent = parent;
	}

	public ModuleState getState() {
		return state;
	}

	public void setState(ModuleState state) {
		this.state = state;
	}

	public List<String> getDependentModuleNames() {
		return Collections.emptyList();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + ((parent == null) ? 0 : parent.hashCode());
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
		final WebPlaceholderModuleDefinition other = (WebPlaceholderModuleDefinition) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		}
		else if (!parent.equals(other.parent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		toString(buffer, 0);
		return buffer.toString();
	}

	public void toString(StringBuffer buffer, int spaces) {
		ModuleDefinitionUtils.addAttributes(spaces, buffer, this);
	}
}
