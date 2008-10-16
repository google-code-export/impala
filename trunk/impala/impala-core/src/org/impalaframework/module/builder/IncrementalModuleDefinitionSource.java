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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.impalaframework.module.TypeReader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.SerializationUtils;
import org.springframework.util.Assert;

/**
 * Implementation of <code>ModuleDefinitionSource</code> which adds module to an existing <code>RootModuleDefinition</code>
 * @author Phil Zoio
 */
public class IncrementalModuleDefinitionSource extends BaseInternalModuleDefinitionSource implements ModuleDefinitionSource {

	private String moduleName;
	private RootModuleDefinition existingDefinition;
	private List<String> modulesToLoad = Collections.emptyList();
	private ModuleDefinition parentDefinition;
	private Map<String, TypeReader> typeReaders;
	
	public IncrementalModuleDefinitionSource(ModuleLocationResolver resolver, Map<String, TypeReader> typeReaders, RootModuleDefinition existingDefinition, String moduleName) {
		this(resolver, typeReaders, existingDefinition, moduleName, true);
	}

	public IncrementalModuleDefinitionSource(ModuleLocationResolver resolver, Map<String, TypeReader> typeReaders, RootModuleDefinition existingDefinition, String moduleName, boolean loadDependendentModules) {
		super(resolver, loadDependendentModules);
		Assert.notNull(moduleName, "moduleName cannot be null");
		Assert.notNull(existingDefinition, "existingDefiniton cannot be null");
		Assert.notNull(typeReaders, "typeReaders cannot be null");
		this.moduleName = moduleName;
		this.existingDefinition = (RootModuleDefinition) SerializationUtils.clone(existingDefinition);
		this.typeReaders = typeReaders;
	}

	/**
	 * Returns new <code>RootModuleDefinition</code>
	 */
	public RootModuleDefinition getModuleDefinition() {
		
		if (existingDefinition.findChildDefinition(moduleName, true) != null) {
			return existingDefinition;
		}
		
		buildMaps();
		String childModule = moduleName;
		ModuleDefinition parent = null;
		
		modulesToLoad = new ArrayList<String>();
		while (parent == null && childModule != null) {
			parent = existingDefinition.getModule(childModule);
			if (parent == null) modulesToLoad.add(childModule);
			childModule = getParents().get(childModule);
		}
		
		if (parent == null) {
			//we're now down to the root module
			parent = existingDefinition;
			//remove the last entry, as this belongs to the root
			modulesToLoad.remove(modulesToLoad.size()-1);
		}

		Collections.reverse(modulesToLoad);
		this.parentDefinition = parent;
		
		ModuleDefinitionSource internalModuleBuilder = getModuleBuilder();
		return internalModuleBuilder.getModuleDefinition();
	}

	protected ModuleDefinitionSource getModuleBuilder() {
		return new IncrementalModuleBuilder(typeReaders, existingDefinition, parentDefinition, getModuleProperties(), modulesToLoad);
	}

	void buildMaps() {
		String[] moduleNames = new String[]{this.moduleName};
		while (moduleNames.length != 0) {
			loadProperties(moduleNames);
			extractParentsAndChildren(moduleNames);
			moduleNames = buildMissingModules();
		}
	}

	List<String> getModulesToLoad() {
		return modulesToLoad;
	}

	void setModulesToLoad(List<String> modulesToLoad) {
		this.modulesToLoad = modulesToLoad;
	}

}
