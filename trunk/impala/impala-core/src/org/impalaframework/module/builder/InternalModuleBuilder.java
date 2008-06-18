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

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.type.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.module.type.TypeReaderUtils;
import org.springframework.util.Assert;

/**
 * Responsible for building <code>RootModuleDefinition</code> from information provided in the form of properties
 * @author Phil Zoio
 */
public class InternalModuleBuilder implements ModuleDefinitionSource {
	
	private Map<String, Properties> moduleProperties;
	private Map<String, Set<String>> children;
	private String rootModuleName;
	private Map<String, TypeReader> typeReaders;
	
	InternalModuleBuilder() {
		this.typeReaders = TypeReaderRegistryFactory.getTypeReaders();
	}
	
	public InternalModuleBuilder(String rootModule, Map<String, Properties> moduleProperties, Map<String, Set<String>> children) {
		super();
		Assert.notNull(rootModule, "rootModuleName cannot be null");
		Assert.notNull(moduleProperties, "moduleProperties cannot be null");
		Assert.notNull(children, "children cannot be null");
		this.moduleProperties = moduleProperties;
		this.children = children;
		this.rootModuleName = rootModule;
		this.typeReaders = TypeReaderRegistryFactory.getTypeReaders();
	}

	public RootModuleDefinition getModuleDefinition() {
		Properties rootModuleProperties = getPropertiesForModule(rootModuleName);
		TypeReader typeReader = TypeReaderUtils.getTypeReader(typeReaders, ModuleTypes.ROOT);
		RootModuleDefinition rootModuleDefinition = readRootModuleDefinition(rootModuleProperties, typeReader);
		
		//recursively build child definitions
		buildChildDefinitions(rootModuleDefinition, rootModuleName);
		return rootModuleDefinition;
	}

	private void buildChildDefinitions(ModuleDefinition parentDefinition, String parentModuleName) {
		Set<String> moduleChildren = children.get(parentModuleName);
		if (moduleChildren != null) {
			for (String moduleName : moduleChildren) {
				Properties properties = moduleProperties.get(moduleName);
				String type = getType(properties);
				TypeReader reader = TypeReaderUtils.getTypeReader(typeReaders, type);
				ModuleDefinition definition = reader.readModuleDefinition(parentDefinition, moduleName, properties);
				definition.setParentDefinition(parentDefinition);
				buildChildDefinitions(definition, moduleName);
			}
		}
	}

	String getType(Properties properties) {
		String type = properties.getProperty(ModuleElementNames.TYPE_ELEMENT);
		if (type == null) {
			type = ModuleTypes.APPLICATION;
		}
		return type;
	}

	private RootModuleDefinition readRootModuleDefinition(Properties rootModuleProperties,
			TypeReader typeReader) {
		ModuleDefinition moduleDefinition = typeReader.readModuleDefinition(null, rootModuleName, rootModuleProperties);
		if (!(moduleDefinition instanceof RootModuleDefinition)) {
			throw new IllegalStateException("Type reader " + typeReader + " produced " + ModuleDefinition.class.getSimpleName() + " which is not an instance of " + RootModuleDefinition.class.getName());
		}
		RootModuleDefinition rootDefinition = (RootModuleDefinition) moduleDefinition;
		return rootDefinition;
	}

	Properties getPropertiesForModule(String moduleName) {
		Properties properties = moduleProperties.get(moduleName);
		Assert.notNull(properties, "Properties for module '" + moduleName + "' cannot be null");
		return properties;
	}

	public void setTypeReader(String typeName, TypeReader typeReader) {
		this.typeReaders.put(typeName, typeReader);
	}
	
}
