package org.impalaframework.module.type;

import java.util.List;
import java.util.Properties;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ApplicationModuleTypeReader implements TypeReader {

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		//FIXME asserts
		//FIXME test
		String[] locationsArray = null;
		
		String contextLocations = properties.getProperty(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT);
		if (StringUtils.hasText(contextLocations)) {
			locationsArray = StringUtils.tokenizeToStringArray(contextLocations, ", ", true, true);
		}
		return newDefinition(parent, moduleName, locationsArray);
	}

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
			String moduleName, Element definitionElement) {
		List<String> contextLocations = TypeReaderUtils.readContextLocations(definitionElement);
		
		String[] locationsArray = contextLocations.toArray(new String[contextLocations.size()]);
		return newDefinition(parent, moduleName, locationsArray);
	}

	protected ModuleDefinition newDefinition(ModuleDefinition parent, String moduleName, String[] locationsArray) {
		return new SimpleModuleDefinition(parent, moduleName, locationsArray);
	}

}
