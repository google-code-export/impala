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

package org.impalaframework.module.type;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.module.spi.TypeReader;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ApplicationModuleTypeReader implements TypeReader {
    
    /**
     * Constructs new {@link ModuleDefinition} from the supplied properties 
     */
    public ModuleDefinition readModuleDefinition(ModuleDefinition parent, String moduleName, Properties properties) {
        Assert.notNull(moduleName, "moduleName cannot be null");
        Assert.notNull(properties, "properties cannot be null");
        
        String[] locationsArray = TypeReaderUtils.readContextLocations(properties);
        String[] dependencyNames = TypeReaderUtils.readDependencyNames(properties);
        Map<String,String> attributes = TypeReaderUtils.readAttributes(properties);
        String runtime = properties.getProperty(ModuleElementNames.RUNTIME_ELEMENT);
        String type = properties.getProperty(ModuleElementNames.TYPE_ELEMENT);
        
        return newDefinition(parent, moduleName, type, locationsArray, dependencyNames, attributes, runtime);
    }

    /**
     * Constructs new {@link ModuleDefinition} from XML using the supplied {@link Element} instance.
     */
    public ModuleDefinition readModuleDefinition(ModuleDefinition parent,
            String moduleName, 
            Element definitionElement) {
        
        List<String> configLocations = TypeReaderUtils.readContextLocations(definitionElement);
        List<String> dependencyNames = TypeReaderUtils.readDependencyNames(definitionElement);
        Map<String,String> attributes = TypeReaderUtils.readAttributes(definitionElement);
        String runtime = TypeReaderUtils.readRuntime(definitionElement);
        String type = TypeReaderUtils.readType(definitionElement);
        
        return newDefinition(parent, moduleName, 
                type, 
                configLocations.toArray(new String[0]), 
                dependencyNames.toArray(new String[0]), 
                attributes, runtime);
    }

    /**
     * Populates the supplied {@link ModuleDefinition} properties from the supplied XML {@link Element}
     */
    public void readModuleDefinitionProperties(Properties properties, 
            String moduleName, 
            Element definitionElement) {
        
        List<String> configLocations = TypeReaderUtils.readContextLocations(definitionElement);
        properties.setProperty(ModuleElementNames.CONFIG_LOCATIONS_ELEMENT, StringUtils.collectionToCommaDelimitedString(configLocations));
        List<String> dependencyNames = TypeReaderUtils.readDependencyNames(definitionElement);
        properties.put(ModuleElementNames.DEPENDENCIES_ELEMENT, StringUtils.collectionToCommaDelimitedString(dependencyNames));
    }

    protected ModuleDefinition newDefinition(ModuleDefinition parent, 
            String moduleName, 
            String type, 
            String[] locationsArray, 
            String[] dependencyNames, 
            Map<String, String> attributes, String runtime) {
        return new SimpleModuleDefinition(parent, moduleName, type, locationsArray, dependencyNames, attributes, runtime);
    }

}
