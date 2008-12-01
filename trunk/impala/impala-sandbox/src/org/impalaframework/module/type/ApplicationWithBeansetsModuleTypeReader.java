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
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.util.XmlDomUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ApplicationWithBeansetsModuleTypeReader extends ApplicationModuleTypeReader {

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		String[] contextLocationsArray = null;
		
		String contextLocations = properties.getProperty(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT);
		if (StringUtils.hasText(contextLocations)) {
			contextLocationsArray = StringUtils.tokenizeToStringArray(contextLocations, ", ", true, true);
		}
		
		String overrides = properties.getProperty(ModuleElementNames.OVERRIDES_ELEMENT);
		return new SimpleBeansetModuleDefinition(parent, moduleName, contextLocationsArray, overrides);
	}

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
			String moduleName, Element definitionElement) {
		List<String> contextLocations = TypeReaderUtils.readContextLocations(definitionElement);
		
		String[] locationsArray = contextLocations.toArray(new String[contextLocations.size()]);
		String overrides = XmlDomUtils.readOptionalElementText(definitionElement, ModuleElementNames.OVERRIDES_ELEMENT);
		return new SimpleBeansetModuleDefinition(parent, moduleName, locationsArray, overrides);
	}

	public void readModuleDefinitionProperties(Properties properties, String moduleName,
			Element definitionElement) {
		super.readModuleDefinitionProperties(properties, moduleName, definitionElement);

		String overrides = XmlDomUtils.readOptionalElementText(definitionElement, ModuleElementNames.OVERRIDES_ELEMENT);
		properties.put(ModuleElementNames.OVERRIDES_ELEMENT, overrides);
	}

}
