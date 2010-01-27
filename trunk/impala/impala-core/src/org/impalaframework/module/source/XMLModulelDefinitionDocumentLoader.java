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

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.XMLDomUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Loads document by calling non-validating
 * @author Phil Zoio
 */
public class XMLModulelDefinitionDocumentLoader {

    protected Document loadDocument(Resource resource) {
        try {
            return XMLDomUtils.loadDocument(resource);
        } catch (Exception e) {
            throw new ConfigurationException("Unable to load XML module definition document from resource " + resource, e);
        }
    }

    protected Document loadDocument(Resource resource, Resource xsd) {
        try {
            final Document document = XMLDomUtils.loadDocument(resource);
            final Element documentElement = document.getDocumentElement();
            
            if (StringUtils.hasText(documentElement.getAttribute("xmlns"))) {
                XMLDomUtils.validateDocument(document, "moduledefinitions.xml document", xsd);
            }
            return document;
        } catch (Exception e) {
            throw new ConfigurationException("Unable to load XML module definition document from resource " + resource, e);
        }
    }
    
}
