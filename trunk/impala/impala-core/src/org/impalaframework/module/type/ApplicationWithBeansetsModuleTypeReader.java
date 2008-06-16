package org.impalaframework.module.type;

import java.util.Properties;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.springframework.util.StringUtils;

public class ApplicationWithBeansetsModuleTypeReader implements TypeReader {

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		//FIXME asserts
		//FIXME test
		String[] contextLocationsArray = null;
		
		String contextLocations = properties.getProperty(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT);
		if (StringUtils.hasText(contextLocations)) {
			contextLocationsArray = StringUtils.tokenizeToStringArray(contextLocations, ", ", true, true);
		}
		
		String overrides = properties.getProperty(ModuleElementNames.OVERRIDES_ELEMENT);
		return new SimpleBeansetModuleDefinition(parent, moduleName, contextLocationsArray, overrides);
	}
	
	boolean isBeanSetDefinition(String type, String overrides) {
		boolean isBeanSetDefinition = overrides != null || ModuleTypes.APPLICATION_WITH_BEANSETS.equalsIgnoreCase(type);
		return isBeanSetDefinition;
	}

}
