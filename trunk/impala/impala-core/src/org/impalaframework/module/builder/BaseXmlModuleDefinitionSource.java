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

package org.impalaframework.module.builder;

import org.impalaframework.module.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.util.XmlDomUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class BaseXmlModuleDefinitionSource implements ModuleDefinitionSource {

	private Resource resource;
	
	private XmlModulelDefinitionDocumentLoader xmlDefinitionLoader;

	public BaseXmlModuleDefinitionSource() {
		this.xmlDefinitionLoader = new XmlModulelDefinitionDocumentLoader();
	}

	protected Element getRootElement() {
		Assert.notNull(resource, "resource cannot be null");
		Document document = xmlDefinitionLoader.loadDocument(resource);

		Element root = document.getDocumentElement();
		return root;
	}

	protected String getType(Element definitionElement) {
		String type = XmlDomUtils.readOptionalElementText(definitionElement, ModuleElementNames.TYPE_ELEMENT);
		if (type == null) {
			type = ModuleTypes.APPLICATION;
		}
		return type;
	}

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
