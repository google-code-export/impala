package org.impalaframework.module.type;

import static org.impalaframework.module.builder.ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT;
import static org.impalaframework.module.builder.ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT;

import java.util.Properties;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.util.StringUtils;

public class RootModuleTypeReader implements TypeReader {

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		//FIXME test
		String configLocations = properties.getProperty(CONTEXT_LOCATIONS_ELEMENT);
		String[] configLocationsArray = StringUtils.tokenizeToStringArray(configLocations, ",", true, true);
		//FIXME throw exception 
		//FIXME should have a default?
		
		//FIXME test
		String rootProjectNames = properties.getProperty(ROOT_PROJECT_NAMES_ELEMENT);
		String[] rootProjectNamesArray = StringUtils.tokenizeToStringArray(rootProjectNames, ",", true, true);
		
		SimpleRootModuleDefinition definition = new SimpleRootModuleDefinition(rootProjectNamesArray, configLocationsArray);
		return definition;
	}

}
