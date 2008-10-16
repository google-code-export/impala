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

import org.impalaframework.module.ModuleElementNames;
import org.impalaframework.module.TypeReader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.module.type.TypeReaderUtils;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public abstract class BasePropertiesModuleBuilder implements ModuleDefinitionSource {
	
	private Map<String, Properties> moduleProperties;
	private Map<String, TypeReader> typeReaders;
	
	BasePropertiesModuleBuilder() {
		this.typeReaders = TypeReaderRegistryFactory.getTypeReaders();
	}
	
	public BasePropertiesModuleBuilder(Map<String, Properties> moduleProperties, Map<String, TypeReader> typeReaders) {
		super();
		Assert.notNull(moduleProperties, "moduleProperties cannot be null");
		Assert.notNull(typeReaders, "typeReaders cannot be null");
		this.moduleProperties = moduleProperties;
		this.typeReaders = typeReaders;
	}


	protected ModuleDefinition buildModuleDefinition(
			ModuleDefinition parentDefinition, String moduleName) {
		Properties properties = moduleProperties.get(moduleName);
		String type = getType(properties);
		TypeReader reader = TypeReaderUtils.getTypeReader(typeReaders, type);
		ModuleDefinition definition = reader.readModuleDefinition(parentDefinition, moduleName, properties);
		definition.setParentDefinition(parentDefinition);
		return definition;
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
