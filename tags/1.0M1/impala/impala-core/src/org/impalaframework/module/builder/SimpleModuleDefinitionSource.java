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

package org.impalaframework.module.builder;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class SimpleModuleDefinitionSource implements ModuleDefinitionSource {

	private RootModuleDefinition parent;

	public SimpleModuleDefinitionSource(String[] rootProjectNames, String[] rootContextLocations, String[] moduleNames) {
		super();
		//TODO add capability of building a hierarchy, or even a portion of a module definition graphs
		this.parent = new SimpleRootModuleDefinition(rootProjectNames, rootContextLocations);
		setModuleNames(this.parent, moduleNames);
	}
	
	public SimpleModuleDefinitionSource(String rootProjectName, String[] rootContextLocations, String[] moduleNames) {
		this(new String[]{rootProjectName}, rootContextLocations, moduleNames);
	}
	
	public SimpleModuleDefinitionSource(String rootProjectName, String rootContextLocation, String[] moduleNames) {
		this(new String[]{rootProjectName}, new String[] { rootContextLocation }, moduleNames);
	}

	public SimpleModuleDefinitionSource(String rootProjectName, String[] moduleNames) {
		super();		
		String[] projectNames = new String[]{rootProjectName};
		this.parent = new SimpleRootModuleDefinition(projectNames, new String[] { "applicationContext.xml" });
		setModuleNames(this.parent, moduleNames);
	}

	public RootModuleDefinition getModuleDefinition() {
		return parent;
	}

	private void setModuleNames(ModuleDefinition parent, String[] moduleNames) {
		Assert.notNull(moduleNames);
		
		ModuleDefinition[] definitions = new ModuleDefinition[moduleNames.length];
		for (int i = 0; i < moduleNames.length; i++) {
			Assert.notNull(moduleNames[i]);
			definitions[i] = new SimpleModuleDefinition(parent, moduleNames[i]);
		}
	}

}
