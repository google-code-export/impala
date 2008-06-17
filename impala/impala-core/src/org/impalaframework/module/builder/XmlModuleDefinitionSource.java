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

import java.util.List;
import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.type.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.module.type.TypeReaderUtils;
import org.impalaframework.util.XmlDomUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlModuleDefinitionSource implements ModuleDefinitionSource {

	private Resource resource;

	private XmlModulelDefinitionDocumentLoader xmlDefinitionLoader;
	
	private Map<String, TypeReader> typeReaders;

	public XmlModuleDefinitionSource() {
		this(TypeReaderRegistryFactory.getTypeReaders());
	}

	protected XmlModuleDefinitionSource(Map<String, TypeReader> typeReaders) {
		super();
		this.typeReaders = typeReaders;
		this.xmlDefinitionLoader = new XmlModulelDefinitionDocumentLoader();
	}

	public RootModuleDefinition getModuleDefinition() {
		Document document = xmlDefinitionLoader.loadDocument(resource);

		Element root = document.getDocumentElement();
		RootModuleDefinition rootModuleDefinition = getRootModuleDefinition(root);

		readChildDefinitions(rootModuleDefinition, root);

		return rootModuleDefinition;
	}

	private void readChildDefinitions(ModuleDefinition definition, Element element) {
		Element definitionsElement = DomUtils.getChildElementByTagName(element, ModuleElementNames.MODULES_ELEMENT);
		if (definitionsElement != null) {
			readDefinitions(definition, definitionsElement);
		}
	}

	@SuppressWarnings("unchecked")
	private void readDefinitions(ModuleDefinition parentDefinition, Element definitionsElement) {
		List<Element> definitionElementList = DomUtils.getChildElementsByTagName(definitionsElement, ModuleElementNames.MODULE_ELEMENT);

		for (Element definitionElement : definitionElementList) {
			
			Element nameElement = DomUtils.getChildElementByTagName(definitionElement, ModuleElementNames.NAME_ELEMENT);
			String name = DomUtils.getTextValue(nameElement);
			Assert.notNull(nameElement, ModuleElementNames.MODULE_ELEMENT + " must contain an element: " + ModuleElementNames.NAME_ELEMENT);

			String type = XmlDomUtils.readOptionalElementText(definitionElement, ModuleElementNames.TYPE_ELEMENT);
			if (type == null) {
				type = ModuleTypes.APPLICATION;
			}
			
			TypeReader typeReader = TypeReaderUtils.getTypeReader(typeReaders, type);
			ModuleDefinition childDefinition = typeReader.readModuleDefinition(parentDefinition, name, definitionElement);

			readChildDefinitions(childDefinition, definitionElement);
		}
	}

	private RootModuleDefinition getRootModuleDefinition(Element root) {
		TypeReader typeReader = TypeReaderUtils.getTypeReader(typeReaders, ModuleTypes.ROOT);
		return (RootModuleDefinition) typeReader.readModuleDefinition(null, RootModuleDefinition.NAME, root);
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
