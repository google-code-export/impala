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

package org.impalaframework.module.source;

import java.util.Map;
import java.util.Properties;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.module.spi.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.springframework.util.Assert;

/**
 * Base class for {@link ModuleDefinitionSource} implementation which builds {@link ModuleDefinition}
 * from a supplied set of module properties.
 * @author Phil Zoio
 */
public abstract class BasePropertiesModuleDefinitionSource implements ModuleDefinitionSource {
    
    private Map<String, Properties> moduleProperties;
    private TypeReaderRegistry typeReadeRegistry;
    
    BasePropertiesModuleDefinitionSource() {
        this.typeReadeRegistry = TypeReaderRegistryFactory.getTypeReaderRegistry();
    }
    
    /**
     * Constructor
     * @param moduleProperties a mapping of module names to module {@link Properties}
     * @param typeReaderRegistry a registry of {@link TypeReader}s
     */
    public BasePropertiesModuleDefinitionSource(Map<String, Properties> moduleProperties, TypeReaderRegistry typeReaderRegistry) {
        super();
        Assert.notNull(moduleProperties, "moduleProperties cannot be null");
        Assert.notNull(typeReaderRegistry, "typeReadeRegistry cannot be null");
        this.moduleProperties = moduleProperties;
        this.typeReadeRegistry = typeReaderRegistry;
    }

    /**
     * Uses the {@link TypeReader} for the specified module to build a {@link ModuleDefinition}
     * instance from a set of module {@link Properties}.
     */
    protected ModuleDefinition buildModuleDefinition(
            ModuleDefinition parentDefinition, String moduleName) {
        Properties properties = moduleProperties.get(moduleName);
        String type = getType(properties);
        TypeReader reader = typeReadeRegistry.getTypeReader(type);
        ModuleDefinition definition = reader.readModuleDefinition(parentDefinition, moduleName, properties);
        definition.setParentDefinition(parentDefinition);
        return definition;
    }

    /**
     * Utility method to extract the module type from the module properties.
     * Assumes the element {@link ModuleElementNames#TYPE_ELEMENT} is used.
     * Defaults to {@link ModuleTypes#APPLICATION}.
     */
    protected String getType(Properties properties) {
        String type = properties.getProperty(ModuleElementNames.TYPE_ELEMENT);
        if (type == null) {
            type = ModuleTypes.APPLICATION;
        }
        return type;
    }

    /**
     * Utility method to extract the module {@link Properties} for a particular module.
     * Throws {@link IllegalArgumentException} if no properties are present for the named
     * module.
     */
    protected Properties getPropertiesForModule(String moduleName) {
        Properties properties = moduleProperties.get(moduleName);
        Assert.notNull(properties, "Properties for module '" + moduleName + "' cannot be null");
        return properties;
    }

    protected TypeReaderRegistry getTypeReadeRegistry() {
        return typeReadeRegistry;
    }
    
}
