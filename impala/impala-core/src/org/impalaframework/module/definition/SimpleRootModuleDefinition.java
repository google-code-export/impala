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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimpleRootModuleDefinition implements RootModuleDefinition {
	
	private static final long serialVersionUID = 1L;
	
	private ChildModuleContainer childContainer;
	
	private List<String> rootContextLocations;
	
	private List<String> dependencies;
	
	private List<ModuleDefinition> siblings;

	private String name;

	private ModuleState state;

	public SimpleRootModuleDefinition(String name, String contextLocations) {
		this(name, new String[]{ contextLocations });
	}
	
	public SimpleRootModuleDefinition(String name, String[] contextLocations) {
		this(name, contextLocations, new String[0], new ModuleDefinition[0]);
	}
	
	public SimpleRootModuleDefinition(
			String name,
			String[] contextLocations,
			String[] dependencies,
			ModuleDefinition[] siblings) {
		
		Assert.notNull(name, "name cannot be null");
		//FIXME should allow these to be null
		Assert.notNull(dependencies, "dependencies cannot be null. Use empty list instead");
		Assert.notNull(siblings, "siblings cannot be null. Use empty list instead");
		
		this.name = name;
		
		if (contextLocations == null || contextLocations.length == 0) {
			contextLocations = ModuleDefinitionUtils.defaultContextLocations(name);
		}
		
		Assert.notEmpty(contextLocations, "rootContextLocations cannot be empty");
		this.rootContextLocations = new ArrayList<String>();
		for (int i = 0; i < contextLocations.length; i++) {
			Assert.notNull(contextLocations[i]);
			this.rootContextLocations.add(contextLocations[i]);
		}
		
		this.childContainer = new ChildModuleContainerImpl();

		this.siblings = Arrays.asList(siblings);
		this.dependencies = Arrays.asList(dependencies);
	}

	public String getName() {
		return name;
	}

	public ModuleDefinition getParentDefinition() {
		//by definition Parent does not have a parent of its own
		return null;
	}
	
	public void setParentDefinition(ModuleDefinition parentDefinition) {
	}

	public Collection<String> getModuleNames() {
		return childContainer.getModuleNames();
	}

	public ModuleDefinition getModule(String moduleName) {
		return childContainer.getModule(moduleName);
	}

	public Collection<ModuleDefinition> getChildDefinitions() {
		return childContainer.getChildDefinitions();
	}

	public boolean hasDefinition(String definitionName) {
		return getModule(definitionName) != null;
	}

	public void add(ModuleDefinition moduleDefinition) {
		childContainer.add(moduleDefinition);
	}

	public ModuleDefinition remove(String moduleName) {
		return childContainer.remove(moduleName);
	}

	public List<String> getContextLocations() {
		return Collections.unmodifiableList(rootContextLocations);
	}

	public String getType() {
		return ModuleTypes.ROOT;
	}
	
	public boolean containsAll(RootModuleDefinition alternative) {
		if (alternative == null)
			return false;

		final List<String> alternativeLocations = alternative.getContextLocations();

		// check that each of the alternatives are contained in
		// rootContextLocations
		for (String alt : alternativeLocations) {
			if (!rootContextLocations.contains(alt)) {
				return false;
			}
		}

		return true;
	}

	public void addContextLocations(RootModuleDefinition alternative) {
		List<String> contextLocations = alternative.getContextLocations();
		for (String location : contextLocations) {
			if (!rootContextLocations.contains(location)){
				rootContextLocations.add(location);
			}
		}
	}	

	public ModuleState getState() {
		return state;
	}

	public void setState(ModuleState state) {
		this.state = state;
	}	

	public List<String> getDependentModuleNames() {
		return Collections.unmodifiableList(dependencies);
	}

	public ModuleDefinition[] getSiblings() {
		return siblings.toArray(new ModuleDefinition[0]);
	}
	
	public boolean hasSibling(String name) {
		return (getSiblingModule(name) != null);
	}

	public ModuleDefinition findChildDefinition(String moduleName, boolean exactMatch) {
		return ModuleDefinitionWalker.walkRootDefinition(this, new ModuleMatchingCallback(moduleName, exactMatch));		
	}

	public ModuleDefinition getSiblingModule(String name) {
		List<ModuleDefinition> newSibs = siblings;
		for (ModuleDefinition moduleDefinition : newSibs) {
			if (moduleDefinition.getName().equals(name)) {
				return moduleDefinition;
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dependencies == null) ? 0 : dependencies.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((rootContextLocations == null) ? 0
						: rootContextLocations.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		//note that equals depends on dependencies, name and context locations
		//it does not depend on siblings and children, as it just a container for these, rather than 
		//using these as a source for its identity
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SimpleRootModuleDefinition other = (SimpleRootModuleDefinition) obj;
		if (dependencies == null) {
			if (other.dependencies != null)
				return false;
		} else if (!dependencies.equals(other.dependencies))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rootContextLocations == null) {
			if (other.rootContextLocations != null)
				return false;
		} else if (!rootContextLocations.equals(other.rootContextLocations))
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
		List<ModuleDefinition> newSibs = siblings;
		
		//FIXME verify that the output displays siblings nicely
		
		for (ModuleDefinition moduleDefinition : newSibs) {
			ModuleDefinitionUtils.addAttributes(spaces, buffer, moduleDefinition);
		}
	}
}
