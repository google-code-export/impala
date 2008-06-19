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

import org.impalaframework.facade.Impala;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;

/**
 * Implementation of <code>ModuleDefinitionSource</code> which adds module to an existing <code>RootModuleDefinition</code>
 * @author Phil Zoio
 */
public class IncrementalModuleDefinitionSource extends BaseInternalModuleDefinitionSource implements ModuleDefinitionSource {

	private String moduleName;
	
	public IncrementalModuleDefinitionSource(String moduleName) {
		this(Impala.getFacade().getModuleLocationResolver(), moduleName);
	}

	public IncrementalModuleDefinitionSource(ModuleLocationResolver resolver, String moduleName) {
		this(resolver, moduleName, true);
	}

	public IncrementalModuleDefinitionSource(ModuleLocationResolver resolver, String moduleName, boolean loadDependendentModules) {
		super(resolver, loadDependendentModules);
		this.moduleName = moduleName;
	}

	public RootModuleDefinition getModuleDefinition() {
		buildMaps();
		
		ModuleDefinitionSource internalModuleBuilder = getModuleBuilder();
		return internalModuleBuilder.getModuleDefinition();
	}

	protected ModuleDefinitionSource getModuleBuilder() {
		return null;
	}

	void buildMaps() {
		String[] moduleNames = new String[]{this.moduleName};
		while (moduleNames.length != 0) {
			loadProperties(moduleNames);
			extractParentsAndChildren(moduleNames);
			moduleNames = buildMissingModules();
		}
	}

}
