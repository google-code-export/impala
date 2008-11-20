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
	
	private List<String> parentContextLocations;

	private List<String> projectNames;

	private ModuleState state;

	public SimpleRootModuleDefinition(String projectName, String contextLocations) {
		this(new String[]{projectName}, new String[]{ contextLocations });
	}
	
	public SimpleRootModuleDefinition(String[] projectNames, String[] contextLocations) {
		super();
		
		Assert.notNull(projectNames);		
		Assert.notEmpty(projectNames, "Root project name list is empty. For example, you may not have set up the root-project-name element in your module definition XML correctly");

		this.projectNames = new ArrayList<String>();
		for (int i = 0; i < projectNames.length; i++) {
			Assert.notNull(projectNames[i]);
			this.projectNames.add(projectNames[i]);
		}
		
		if (contextLocations == null || contextLocations.length == 0) {
			contextLocations = ModuleDefinitionUtils.defaultContextLocations(projectNames[0]);
		}
		
		Assert.notEmpty(contextLocations, "parentContextLocations cannot be empty");
		this.parentContextLocations = new ArrayList<String>();
		for (int i = 0; i < contextLocations.length; i++) {
			Assert.notNull(contextLocations[i]);
			this.parentContextLocations.add(contextLocations[i]);
		}
		
		this.childContainer = new ChildModuleContainerImpl();
	}
	
	public SimpleRootModuleDefinition(List<String> projectNames, List<String> contextLocations) {
		super();
		Assert.notNull(contextLocations);
		Assert.notNull(projectNames);
		Assert.notEmpty(projectNames, "Root project name list is empty. For example, you may not have set up the root-project-name element in your module definition XML correctly");
		this.projectNames = new ArrayList<String>(projectNames);
		
		if (contextLocations.isEmpty()) {
			contextLocations = Arrays.asList(ModuleDefinitionUtils.defaultContextLocations(projectNames.get(0)));
		}
		
		this.parentContextLocations = new ArrayList<String>(contextLocations);
		this.childContainer = new ChildModuleContainerImpl();
	}

	public String getName() {
		return projectNames.get(0);
	}

	public ModuleDefinition getParentDefinition() {
		//by definition Parent does not have a parent of its own
		return null;
	}
	
	public ModuleDefinition findChildDefinition(String moduleName, boolean exactMatch) {
		return ModuleDefinitionUtils.findDefinition(moduleName, this, exactMatch);
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
		return Collections.unmodifiableList(parentContextLocations);
	}

	public String getType() {
		return ModuleTypes.ROOT;
	}
	
	public boolean containsAll(RootModuleDefinition alternative) {
		if (alternative == null)
			return false;

		final List<String> alternativeLocations = alternative.getContextLocations();

		// check that each of the alternatives are contained in
		// parentContextLocations
		for (String alt : alternativeLocations) {
			if (!parentContextLocations.contains(alt)) {
				return false;
			}
		}

		return true;
	}

	public void addContextLocations(RootModuleDefinition alternative) {
		List<String> contextLocations = alternative.getContextLocations();
		for (String location : contextLocations) {
			if (!parentContextLocations.contains(location)){
				parentContextLocations.add(location);
			}
		}
	}	

	public ModuleState getState() {
		return state;
	}

	public void setState(ModuleState state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parentContextLocations == null) ? 0 : parentContextLocations.hashCode());
		result = prime * result + ((projectNames == null) ? 0 : projectNames.hashCode());
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
		final SimpleRootModuleDefinition other = (SimpleRootModuleDefinition) obj;
		if (parentContextLocations == null) {
			if (other.parentContextLocations != null)
				return false;
		}
		else if (!parentContextLocations.equals(other.parentContextLocations))
			return false;
		if (projectNames == null) {
			if (other.projectNames != null)
				return false;
		}
		else if (!projectNames.equals(other.projectNames))
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
