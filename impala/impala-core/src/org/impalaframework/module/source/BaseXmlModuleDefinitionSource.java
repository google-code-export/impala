/*
 * Copyright 2007-2008 the original author or authors.
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

import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.util.XMLDomUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Base class for {@link ModuleDefinitionSource} implementations which read {@link RootModuleDefinition}
 * from Impala's module XML format.
 * @author Phil Zoio
 */
public abstract class BaseXmlModuleDefinitionSource implements ModuleDefinitionSource {

    private Resource resource;
    
    private XMLModulelDefinitionDocumentLoader xmlDefinitionLoader;

    public BaseXmlModuleDefinitionSource() {
        this.xmlDefinitionLoader = new XMLModulelDefinitionDocumentLoader();
    }

    /**
     * Returns a reference for the root element from the {@link Resource}
     * from which the module definition is being loaded.
     * @return an instance of {@link Element}
     */
    protected Element getRootElement() {
        Assert.notNull(resource, "resource cannot be null");
        Document document = xmlDefinitionLoader.loadDocument(resource, new ClassPathResource("org/impalaframework/module/source/moduledefinition.xsd"));

        Element root = document.getDocumentElement();
        return root;
    }

    /**
     * Utility method to read the module type from the supplied {@link Element}.
     * Looks for the value enclosed by a {@link ModuleElementNames#TYPE_ELEMENT} subelement of the 
     * supplied element. If none is found, the defaults to {@link ModuleTypes#APPLICATION}.
     */
    protected String getType(Element definitionElement) {
        String type = XMLDomUtils.readOptionalElementText(definitionElement, ModuleElementNames.TYPE_ELEMENT);
        if (type == null) {
            type = ModuleTypes.APPLICATION;
        }
        return type;
    }

    /**
     * Returns the value of the <code>name</code> subelement of the supplied element.
     * Throws {@link IllegalArgumentException} if no value is found.
     */
    protected String getName(Element definitionElement) {
        Element nameElement = DomUtils.getChildElementByTagName(definitionElement, ModuleElementNames.NAME_ELEMENT);
        String name = DomUtils.getTextValue(nameElement);
        Assert.notNull(nameElement, ModuleElementNames.MODULE_ELEMENT + " must contain an element: " + ModuleElementNames.NAME_ELEMENT);
        return name;
    }

    protected Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
