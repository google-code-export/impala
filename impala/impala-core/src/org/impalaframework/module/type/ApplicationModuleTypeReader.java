package org.impalaframework.module.type;

import java.util.Properties;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.util.StringUtils;

public class ApplicationModuleTypeReader implements TypeReader {

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		//FIXME asserts
		//FIXME test
		String[] contextLocationsArray = null;
		
		String contextLocations = properties.getProperty(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT);
		if (StringUtils.hasText(contextLocations)) {
			contextLocationsArray = StringUtils.tokenizeToStringArray(contextLocations, ", ", true, true);
		}
		SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition(parent, moduleName, contextLocationsArray);
		return moduleDefinition;
	}

}
