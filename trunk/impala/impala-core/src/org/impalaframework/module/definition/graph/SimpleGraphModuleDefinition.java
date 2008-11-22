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

package org.impalaframework.module.definition.graph;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.util.ArrayUtils;

public class SimpleGraphModuleDefinition extends SimpleModuleDefinition implements GraphModuleDefinition {

	private static final long serialVersionUID = 1L;
	
	private List<String> dependencies;
	
	public SimpleGraphModuleDefinition(String name) {
		super(name);
		this.dependencies = new ArrayList<String>();
	}
	
	public SimpleGraphModuleDefinition(String name, String[] dependencies) {
		this(null, dependencies, name);
	}

	public SimpleGraphModuleDefinition(ModuleDefinition parent, String[] dependencies, String name) {
		super(parent, name);
		this.dependencies = ArrayUtils.toList(dependencies);
	}
	
	public String[] getDependentModuleNames() {
		final ModuleDefinition parentDefinition = getParentDefinition();
		
		if (parentDefinition != null) {
			final String parentName = parentDefinition.getName();
			if (!dependencies.contains(parentName))
			{
				dependencies.add(0, parentName);
			}
		}
		return dependencies.toArray(new String[0]);
	}

	@Override
	public void toString(StringBuffer buffer, int spaces) {
		ModuleDefinitionUtils.addAttributes(spaces, buffer, this);
		buffer.append(", dependencies=" + dependencies);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dependencies == null) ? 0 : dependencies.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SimpleGraphModuleDefinition other = (SimpleGraphModuleDefinition) obj;
		if (dependencies == null) {
			if (other.dependencies != null)
				return false;
		} else if (!dependencies.equals(other.dependencies))
			return false;
		return true;
	}

}
