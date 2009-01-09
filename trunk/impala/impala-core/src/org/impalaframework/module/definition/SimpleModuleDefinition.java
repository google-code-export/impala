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

import java.util.Map;

import org.impalaframework.module.ModuleDefinition;

/**
 * @author Phil Zoio
 */
public class SimpleModuleDefinition extends BaseModuleDefinition {
	
	private static final long serialVersionUID = 1L;

	/* ********************* constructors ******************** */

	public SimpleModuleDefinition(String name) {
		this(null, name, ModuleDefinitionUtils.defaultContextLocations(name));
	}

	public SimpleModuleDefinition(ModuleDefinition parent, String name) {
		this(parent, name, ModuleDefinitionUtils.defaultContextLocations(name));
	}

	public SimpleModuleDefinition(ModuleDefinition parent, String name, String[] contextLocations) {
		this(parent, name, contextLocations, null, null);
	}
	
	public SimpleModuleDefinition(
			ModuleDefinition parent, 
			String name, 
			String[] contextLocations, 
			String[] dependencies, 
			Map<String, String> attributes) {
		super(parent, name, dependencies, contextLocations, attributes);
	}

	/* ********************* read-only methods ******************** */

	public ModuleDefinition findChildDefinition(String moduleName, boolean exactMatch) {
		return ModuleDefinitionWalker.walkModuleDefinition(this, new ModuleMatchingCallback(moduleName, exactMatch));
	}

	public String getType() {
		return ModuleTypes.APPLICATION;
	}
	
}
