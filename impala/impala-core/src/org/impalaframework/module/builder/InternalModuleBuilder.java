package org.impalaframework.module.builder;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.type.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.springframework.util.Assert;

public class InternalModuleBuilder implements ModuleDefinitionSource {
	
	private Map<String, Properties> moduleProperties;
	private Map<String, Set<String>> children;
	private String rootModuleName;
	private Map<String, TypeReader> typeReaders;
	
	public InternalModuleBuilder(String rootModule, Map<String, Properties> moduleProperties, Map<String, Set<String>> children) {
		super();
		Assert.notNull(rootModule, "rootModuleName cannot be null");
		Assert.notNull(moduleProperties, "moduleProperties cannot be null");
		Assert.notNull(children, "children cannot be null");
		this.moduleProperties = moduleProperties;
		this.children = children;
		this.rootModuleName = rootModule;
		this.typeReaders = TypeReaderRegistryFactory.readTypeReaders();
	}

	public RootModuleDefinition getModuleDefinition() {
		Properties rootModuleProperties = getPropertiesForModule(rootModuleName);
		TypeReader typeReader = getTypeReader(ModuleTypes.ROOT);
		RootModuleDefinition rootModuleDefinition = readRootModuleDefinition(rootModuleProperties, typeReader);
		
		//recursively build child definitions
		buildChildDefinitions(rootModuleDefinition, rootModuleName);
		return rootModuleDefinition;
	}

	private void buildChildDefinitions(ModuleDefinition parentDefinition, String parentModuleName) {
		Set<String> moduleChildren = children.get(parentModuleName);
		if (moduleChildren != null) {
			for (String moduleName : moduleChildren) {
				Properties properties = moduleProperties.get(moduleName);
				String type = getType(properties);
				TypeReader reader = getTypeReader(type);
				ModuleDefinition definition = reader.readModuleDefinition(parentDefinition, moduleName, properties);
				definition.setParentDefinition(parentDefinition);
				//parentDefinition.add(definition);
				buildChildDefinitions(definition, moduleName);
			}
		}
	}

	String getType(Properties properties) {
		//FIXME test
		String type = properties.getProperty(ModuleElementNames.TYPE_ELEMENT);
		if (type == null) {
			type = ModuleTypes.APPLICATION;
		}
		return type;
	}

	private RootModuleDefinition readRootModuleDefinition(Properties rootModuleProperties,
			TypeReader typeReader) {
		ModuleDefinition moduleDefinition = typeReader.readModuleDefinition(null, null, rootModuleProperties);
		//FIXME assert instance of
		RootModuleDefinition rootDefinition = (RootModuleDefinition) moduleDefinition;
		return rootDefinition;
	}

	TypeReader getTypeReader(String typeName) {
		//FIXME test
		TypeReader typeReader = typeReaders.get(typeName.toLowerCase());
		if (typeReader == null) {
			throw new ConfigurationException("No " + TypeReader.class.getName() + " specified for type '" + typeName + "'");
		}
		return typeReader;
	}

	Properties getPropertiesForModule(String moduleName) {
		Properties properties = moduleProperties.get(moduleName);
		Assert.notNull(properties, "Properties for module '" + moduleName + "' cannot be null");
		return properties;
	}
	
}
