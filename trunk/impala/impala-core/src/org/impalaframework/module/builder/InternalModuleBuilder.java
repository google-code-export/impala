package org.impalaframework.module.builder;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static org.impalaframework.module.builder.ModuleElementNames.*;

public class InternalModuleBuilder implements ModuleDefinitionSource {
	
	private Map<String, Properties> moduleProperties;
	private Map<String, Set<String>> children;
	private String rootModule;
	
	public InternalModuleBuilder(String rootModule, Map<String, Properties> moduleProperties, Map<String, Set<String>> children) {
		super();
		Assert.notNull(rootModule, "rootModule cannot be null");
		Assert.notNull(moduleProperties, "moduleProperties cannot be null");
		Assert.notNull(children, "children cannot be null");
		this.moduleProperties = moduleProperties;
		this.children = children;
		this.rootModule = rootModule;
	}

	public RootModuleDefinition getModuleDefinition() {
		Properties rootModuleProperties = getPropertiesForModule(rootModule);
		RootModuleDefinition rootModuleDefinition = buildRootModuleDefinition(rootModuleProperties);
		
		
		return null;
	}

	Properties getPropertiesForModule(String moduleName) {
		Properties properties = moduleProperties.get(moduleName);
		Assert.notNull(properties, "Properties for module '" + moduleName + "' cannot be null");
		return properties;
	}

	private RootModuleDefinition buildRootModuleDefinition(Properties rootModuleProperties) {
		//FIXME test
		String configLocations = rootModuleProperties.getProperty(CONTEXT_LOCATIONS_ELEMENT);
		String[] configLocationsArray = StringUtils.tokenizeToStringArray(configLocations, ",", true, true);
		//FIXME throw exception 
		//FIXME should have a default?
		
		//FIXME test
		String rootProjectNames = rootModuleProperties.getProperty(ROOT_PROJECT_NAMES_ELEMENT);
		String[] rootProjectNamesArray = StringUtils.tokenizeToStringArray(rootProjectNames, ",", true, true);
		
		SimpleRootModuleDefinition definition = new SimpleRootModuleDefinition(rootProjectNamesArray, configLocationsArray);
		return definition;
	}
	
}
