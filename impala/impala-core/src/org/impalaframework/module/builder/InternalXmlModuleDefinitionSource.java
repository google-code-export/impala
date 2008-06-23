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
import java.util.Properties;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.type.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.module.type.TypeReaderUtils;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.XmlDomUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class InternalXmlModuleDefinitionSource implements ModuleDefinitionSource {

	private Resource resource;

	private ModuleLocationResolver moduleLocationResolver;
	
	private XmlModulelDefinitionDocumentLoader xmlDefinitionLoader;
	
	private Map<String, TypeReader> typeReaders;

	public InternalXmlModuleDefinitionSource(ModuleLocationResolver moduleLocationResolver) {
		this(moduleLocationResolver, TypeReaderRegistryFactory.getTypeReaders());
	}

	protected InternalXmlModuleDefinitionSource(ModuleLocationResolver moduleLocationResolver, Map<String, TypeReader> typeReaders) {
		super();
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		Assert.notNull(typeReaders, "typeReaders cannot be null");
		this.typeReaders = typeReaders;
		this.xmlDefinitionLoader = new XmlModulelDefinitionDocumentLoader();
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public RootModuleDefinition getModuleDefinition() {
		Assert.notNull(resource, "resource cannot be null");
		Document document = xmlDefinitionLoader.loadDocument(resource);

		Element root = document.getDocumentElement();
		Element namesElement = DomUtils.getChildElementByTagName(root, ModuleElementNames.NAMES_ELEMENT);
		
		if (namesElement == null) {
			throw new ConfigurationException("Resource '" + resource + "' contains a non-empty '" + ModuleElementNames.NAMES_ELEMENT + "' element, which is illegal when using " + InternalModuleDefinitionSource.class.getSimpleName());
		}
		
		String value = namesElement.getTextContent();
		String[] moduleNames = StringUtils.tokenizeToStringArray(value, " ,\n\r", true, true);
		
		InternalModuleDefinitionSource internalModuleSource = new InternalModuleDefinitionSource(moduleLocationResolver, moduleNames);
		
		//now need to tweak properties with XML variants
		internalModuleSource.inspectModules();
		Map<String, Properties> moduleProperties = internalModuleSource.getModuleProperties();
		
		readChildDefinitions(root, moduleProperties);
		
		return internalModuleSource.buildModules();
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
			
			Element nameElement = DomUtils.getChildElementByTagName(definitionElement, ModuleElementNames.NAME_ELEMENT);
			String name = DomUtils.getTextValue(nameElement);
			Assert.notNull(nameElement, ModuleElementNames.MODULE_ELEMENT + " must contain an element: " + ModuleElementNames.NAME_ELEMENT);

			String type = XmlDomUtils.readOptionalElementText(definitionElement, ModuleElementNames.TYPE_ELEMENT);
			if (type == null) {
				type = ModuleTypes.APPLICATION;
			}
		
			Properties properties = moduleProperties.get(name);
		
			if (properties == null) {
				throw new ConfigurationException("Resource '" + resource + "' contains no new properties for module '" + name + 
						"'. Has this module been declared in the '" + ModuleElementNames.NAMES_ELEMENT + "' element?");
			}
			
			TypeReader typeReader = TypeReaderUtils.getTypeReader(typeReaders, type);
			typeReader.readModuleDefinitionProperties(properties, name, definitionElement);
			
		}
	}
	
	
	
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
