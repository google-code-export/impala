/*
 * Copyright 2007-2010 the original author or authors.
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

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.util.XMLDomUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ApplicationWithBeansetsModuleTypeReader extends ApplicationModuleTypeReader {

	public static final String OVERRIDES_ELEMENT = "overrides";
	
	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		String[] configLocationsArray = null;
		
		String configLocations = properties.getProperty(ModuleElementNames.CONFIG_LOCATIONS_ELEMENT);
		if (StringUtils.hasText(configLocations)) {
			configLocationsArray = StringUtils.tokenizeToStringArray(configLocations, ", ", true, true);
		}
		
		String overrides = properties.getProperty(OVERRIDES_ELEMENT);
		return new SimpleBeansetModuleDefinition(parent, moduleName, configLocationsArray, overrides);
	}

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
			String moduleName, Element definitionElement) {
		List<String> configLocations = TypeReaderUtils.readContextLocations(definitionElement);
		
		String[] locationsArray = configLocations.toArray(new String[configLocations.size()]);
		String overrides = XMLDomUtils.readOptionalElementText(definitionElement, OVERRIDES_ELEMENT);
		return new SimpleBeansetModuleDefinition(parent, moduleName, locationsArray, overrides);
	}

	public void readModuleDefinitionProperties(Properties properties, String moduleName,
			Element definitionElement) {
		super.readModuleDefinitionProperties(properties, moduleName, definitionElement);

		String overrides = XMLDomUtils.readOptionalElementText(definitionElement, OVERRIDES_ELEMENT);
		properties.put(OVERRIDES_ELEMENT, overrides);
	}

}
