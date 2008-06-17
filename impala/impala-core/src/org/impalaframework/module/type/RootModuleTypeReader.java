package org.impalaframework.module.type;

import static org.impalaframework.module.builder.ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT;
import static org.impalaframework.module.builder.ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT;

import java.util.List;
import java.util.Properties;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

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

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
			String moduleName, Element definitionElement) {
		List<String> locationNames = TypeReaderUtils.readContextLocations(definitionElement, ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, ModuleElementNames.CONTEXT_LOCATION_ELEMENT);
		
		// extra check to make sure parent definition had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(definitionElement, ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT), ModuleElementNames.ROOT_MODULE_ELEMENT
					+ " must contain a child element:" + ModuleElementNames.CONTEXT_LOCATION_ELEMENT);
		}
		
		List<String> projectNames = TypeReaderUtils.readContextLocations(definitionElement, ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT, ModuleElementNames.NAME_ELEMENT);
		
		// extra check to make sure parent definition had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(definitionElement, ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT), ModuleElementNames.ROOT_MODULE_ELEMENT
					+ " must contain a child element:" + ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT);
		}
		
		RootModuleDefinition rootModuleDefinition = new SimpleRootModuleDefinition(projectNames, locationNames);
		return rootModuleDefinition;
	}
	
}
