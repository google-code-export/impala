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

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.module.spi.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class InternalXmlModuleDefinitionSource extends BaseXmlModuleDefinitionSource {
    
    private ModuleLocationResolver moduleLocationResolver;
    
    private TypeReaderRegistry typeReaderRegistry;

    public InternalXmlModuleDefinitionSource(ModuleLocationResolver moduleLocationResolver) {
        this(moduleLocationResolver, TypeReaderRegistryFactory.getTypeReaderRegistry());
    }

    public InternalXmlModuleDefinitionSource(ModuleLocationResolver moduleLocationResolver, TypeReaderRegistry typeReaderRegistry) {
        super();
        Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
        Assert.notNull(typeReaderRegistry, "typeReaderRegistry cannot be null");
        this.typeReaderRegistry = typeReaderRegistry;
        this.moduleLocationResolver = moduleLocationResolver;
    }

    public RootModuleDefinition getModuleDefinition() {
        Element root = getRootElement();
        String[] moduleNames = getModuleNames(root);
        
        InternalModuleDefinitionSource internalModuleSource = new InternalModuleDefinitionSource(typeReaderRegistry, moduleLocationResolver, moduleNames);
        
        //now need to tweak properties with XML variants
        internalModuleSource.inspectModules();
        Map<String, Properties> moduleProperties = internalModuleSource.getModuleProperties();
        readChildDefinitions(root, moduleProperties);
        
        return internalModuleSource.buildModules();
    }

    private String[] getModuleNames(Element root) {
        Element namesElement = DomUtils.getChildElementByTagName(root, ModuleElementNames.NAMES_ELEMENT);
        
        if (namesElement == null) {
            throw new ConfigurationException("Resource '" + getResource() + "' contains a non-empty '" + ModuleElementNames.NAMES_ELEMENT + "' element, which is illegal when using " + InternalModuleDefinitionSource.class.getSimpleName());
        }
        
        String value = namesElement.getTextContent();
        String[] moduleNames = StringUtils.tokenizeToStringArray(value, " ,\n\r", true, true);
        return moduleNames;
    }
    
    
    private void readChildDefinitions(Element element, Map<String, Properties> moduleProperties) {
        Element definitionsElement = DomUtils.getChildElementByTagName(element, ModuleElementNames.MODULES_ELEMENT);
        if (definitionsElement != null) {
            readDefinitions(definitionsElement, moduleProperties);
        }
    }

    @SuppressWarnings("unchecked")
    private void readDefinitions(Element definitionsElement, Map<String, Properties> moduleProperties) {
        List<Element> definitionElementList = DomUtils.getChildElementsByTagName(definitionsElement, ModuleElementNames.MODULE_ELEMENT);

        for (Element definitionElement : definitionElementList) {
            
            String name = getName(definitionElement);
            String type = getType(definitionElement);
        
            Properties properties = moduleProperties.get(name);
        
            if (properties == null) {
                throw new ConfigurationException("Resource '" + getResource() + "' contains no new properties for module '" + name + 
                        "'. Has this module been declared in the '" + ModuleElementNames.NAMES_ELEMENT + "' element?");
            }
            
            TypeReader typeReader = typeReaderRegistry.getTypeReader(type);
            typeReader.readModuleDefinitionProperties(properties, name, definitionElement);
        }
    }

}
