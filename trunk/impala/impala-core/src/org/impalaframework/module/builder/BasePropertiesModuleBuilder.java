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
import org.impalaframework.module.type.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.module.type.TypeReaderUtils;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public abstract class BasePropertiesModuleBuilder implements ModuleDefinitionSource {
	
	private Map<String, Properties> moduleProperties;
	private Map<String, Set<String>> children;
	private Map<String, TypeReader> typeReaders;
	
	BasePropertiesModuleBuilder() {
		this.typeReaders = TypeReaderRegistryFactory.getTypeReaders();
	}
	
	public BasePropertiesModuleBuilder(Map<String, Properties> moduleProperties, Map<String, Set<String>> children) {
		super();
		this.moduleProperties = moduleProperties;
		this.children = children;
		this.typeReaders = TypeReaderRegistryFactory.getTypeReaders();
	}

	protected void buildChildDefinitions(ModuleDefinition parentDefinition, String parentModuleName) {
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

	protected String getType(Properties properties) {
		String type = properties.getProperty(ModuleElementNames.TYPE_ELEMENT);
		if (type == null) {
			type = ModuleTypes.APPLICATION;
		}
		return type;
	}

	protected Properties getPropertiesForModule(String moduleName) {
		Properties properties = moduleProperties.get(moduleName);
		Assert.notNull(properties, "Properties for module '" + moduleName + "' cannot be null");
		return properties;
	}

	public void setTypeReader(String typeName, TypeReader typeReader) {
		this.typeReaders.put(typeName, typeReader);
	}

	protected Map<String, TypeReader> getTypeReaders() {
		return typeReaders;
	}
	
}
