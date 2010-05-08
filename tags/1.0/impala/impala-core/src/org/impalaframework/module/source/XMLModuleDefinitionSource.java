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

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.module.spi.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class XMLModuleDefinitionSource extends BaseXmlModuleDefinitionSource {
        
    private TypeReaderRegistry typeReaderRegistry;
    private RootModuleDefinition rootModuleDefinition;

    public XMLModuleDefinitionSource() {
        this(TypeReaderRegistryFactory.getTypeReaderRegistry());
    }

    protected XMLModuleDefinitionSource(TypeReaderRegistry typeReaderRegistry) {
        super();
        this.typeReaderRegistry = typeReaderRegistry;
    }

    public RootModuleDefinition getModuleDefinition() {
        Element root = getRootElement();
        
        rootModuleDefinition = getRootModuleDefinition(root);
        readChildDefinitions(rootModuleDefinition, root, ModuleElementNames.MODULES_ELEMENT);
        readChildDefinitions(null, root, ModuleElementNames.SIBLINGS_ELEMENT);

        return rootModuleDefinition;
    }

    private void readChildDefinitions(ModuleDefinition definition, Element element, String elementName) {
        Element definitionsElement = DomUtils.getChildElementByTagName(element, elementName);
        if (definitionsElement != null) {
            readDefinitions(definition, definitionsElement);
        }
    }

    @SuppressWarnings("unchecked")
    private void readDefinitions(ModuleDefinition parentDefinition, Element definitionsElement) {
        List<Element> definitionElementList = DomUtils.getChildElementsByTagName(definitionsElement, ModuleElementNames.MODULE_ELEMENT);

        for (Element definitionElement : definitionElementList) {
            
            String name = getName(definitionElement);
            String type = getType(definitionElement);
            
            TypeReader typeReader = typeReaderRegistry.getTypeReader(type);
            ModuleDefinition childDefinition = typeReader.readModuleDefinition(parentDefinition, name, definitionElement);
            
            if (parentDefinition == null) {
                //no parent definition is null, this must be the case where the siblings
                rootModuleDefinition.addSibling(childDefinition);
            }

            readChildDefinitions(childDefinition, definitionElement, ModuleElementNames.MODULES_ELEMENT);
        }
    }

    private RootModuleDefinition getRootModuleDefinition(Element root) {
        TypeReader typeReader = typeReaderRegistry.getTypeReader(ModuleTypes.ROOT);
        String name = getName(root);
        return (RootModuleDefinition) typeReader.readModuleDefinition(null, name, root);
    }

}
