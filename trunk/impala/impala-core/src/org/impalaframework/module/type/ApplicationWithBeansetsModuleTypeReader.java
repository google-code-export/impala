package org.impalaframework.module.type;

import java.util.List;
import java.util.Properties;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.util.XmlDomUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ApplicationWithBeansetsModuleTypeReader extends ApplicationModuleTypeReader implements TypeReader {

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

	public void readModuleDefinitionProperties(Properties properties, ModuleDefinition parent,
			String moduleName, Element definitionElement) {
		//FIXME test
		super.readModuleDefinitionProperties(properties, parent, moduleName, definitionElement);

		String overrides = XmlDomUtils.readOptionalElementText(definitionElement, ModuleElementNames.OVERRIDES_ELEMENT);
		properties.put(ModuleElementNames.OVERRIDES_ELEMENT, overrides);
	}

}
