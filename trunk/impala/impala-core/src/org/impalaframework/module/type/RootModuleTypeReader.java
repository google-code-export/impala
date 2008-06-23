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

	public static final String DEFAULT_ROOT_CONTEXT_LOCATION = "root-context.xml";
	
	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
		Assert.isNull(parent, "Root module cannot have a non-null parent");
		Assert.notNull(moduleName, "moduleName not set");
		Assert.notNull(properties, "properties not set");
		
		String configLocations = properties.getProperty(CONTEXT_LOCATIONS_ELEMENT);
		String[] configLocationsArray = StringUtils.tokenizeToStringArray(configLocations, ",", true, true);
		
		if (configLocationsArray == null || configLocationsArray.length == 0) {
			configLocationsArray = new String[]{ DEFAULT_ROOT_CONTEXT_LOCATION };
		}
		
		String rootProjectNames = properties.getProperty(ROOT_PROJECT_NAMES_ELEMENT);
		String[] rootProjectNamesArray = StringUtils.tokenizeToStringArray(rootProjectNames, ",", true, true);
		if (rootProjectNamesArray == null || rootProjectNamesArray.length == 0) {
			rootProjectNamesArray = new String[]{ moduleName };
		}		
		
		SimpleRootModuleDefinition definition = new SimpleRootModuleDefinition(rootProjectNamesArray, configLocationsArray);
		return definition;
	}

	public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Element definitionElement) {
		Assert.isNull(parent, "Root module cannot have a non-null parent");
		Assert.notNull(definitionElement, "definitionElement not set");		
		
		List<String> locationNames = getLocationNames(definitionElement);
		
		List<String> projectNames = getRootProjectNames(definitionElement,
				locationNames);
		
		RootModuleDefinition rootModuleDefinition = new SimpleRootModuleDefinition(projectNames, locationNames);
		return rootModuleDefinition;
	}

	public void readModuleDefinitionProperties(Properties properties, String moduleName,
			Element definitionElement) {
		
		//FIXME test
		List<String> locationNames = getLocationNames(definitionElement);
		List<String> projectNames = getRootProjectNames(definitionElement, locationNames);
		
		properties.put(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, StringUtils.collectionToCommaDelimitedString(locationNames));
		properties.put(ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT, StringUtils.collectionToCommaDelimitedString(projectNames));
	}

	List<String> getRootProjectNames(Element definitionElement,
			List<String> locationNames) {
		List<String> projectNames = TypeReaderUtils.readContextLocations(definitionElement, ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT, ModuleElementNames.NAME_ELEMENT);
		
		// extra check to make sure parent definition had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(definitionElement, ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT), ModuleElementNames.ROOT_MODULE_ELEMENT
					+ " must contain a child element:" + ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT);
		}
		return projectNames;
	}

	List<String> getLocationNames(Element definitionElement) {
		List<String> locationNames = TypeReaderUtils.readContextLocations(definitionElement, ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, ModuleElementNames.CONTEXT_LOCATION_ELEMENT);
		
		// extra check to make sure parent definition had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(definitionElement, ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT), ModuleElementNames.ROOT_MODULE_ELEMENT
					+ " must contain a child element:" + ModuleElementNames.CONTEXT_LOCATION_ELEMENT);
		}
		return locationNames;
	}
	
}
