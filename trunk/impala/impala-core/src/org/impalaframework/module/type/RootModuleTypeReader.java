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

package org.impalaframework.module.type;

import java.util.List;
import java.util.Properties;

import org.impalaframework.module.ModuleElementNames;
import org.impalaframework.module.TypeReader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class RootModuleTypeReader implements TypeReader {
	
	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		Assert.isNull(parent, "Root module cannot have a non-null parent");
		Assert.notNull(moduleName, "moduleName not set");
		Assert.notNull(properties, "properties not set");

		String[] configLocations = TypeReaderUtils.readContextLocations(properties);
		String[] dependencyNames = TypeReaderUtils.readDependencyNames(properties);
		
		SimpleRootModuleDefinition definition = new SimpleRootModuleDefinition(moduleName, configLocations, dependencyNames, null);
		return definition;
	}

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Element definitionElement) {
		Assert.isNull(parent, "Root module cannot have a non-null parent");
		Assert.notNull(definitionElement, "definitionElement not set");
		
		List<String> locationNames = TypeReaderUtils.readContextLocations(definitionElement);
		List<String> dependencyNames = TypeReaderUtils.readDependencyNames(definitionElement);
		
		RootModuleDefinition rootModuleDefinition = new SimpleRootModuleDefinition(moduleName, 
				locationNames.toArray(new String[0]), 
				dependencyNames.toArray(new String[0]),
				new ModuleDefinition[0]);
		return rootModuleDefinition;
	}

	public void readModuleDefinitionProperties(Properties properties, String moduleName,
			Element definitionElement) {

		List<String> locationNames = TypeReaderUtils.readContextLocations(definitionElement);		
		properties.put(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, StringUtils.collectionToCommaDelimitedString(locationNames));
		List<String> dependencyNames = TypeReaderUtils.readDependencyNames(definitionElement);
		properties.put(ModuleElementNames.DEPENDENCIES_ELEMENT, StringUtils.collectionToCommaDelimitedString(dependencyNames));
	}
	
}
