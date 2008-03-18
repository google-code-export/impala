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

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlModuleDefinitionSource implements ModuleDefinitionSource {

	String ROOT_MODULE_ELEMENT = "root";
	
	String ROOT_PROJECT_NAMES_ELEMENT = "root-project-names";

	String CONTEXT_LOCATIONS_ELEMENT = "context-locations";

	String CONTEXT_LOCATION_ELEMENT = "context-location";
	
	String TYPE_ELEMENT = "type";

	String MODULES_ELEMENT = "modules";

	String MODULE_ELEMENT = "module";

	String NAME_ELEMENT = "name";

	String OVERRIDES_ELEMENT = "overrides";

	private Resource resource;

	private XmlModulelDefinitionDocumentLoader xmlDefinitionLoader;

	public XmlModuleDefinitionSource() {
		super();
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
		Element definitionsElement = DomUtils.getChildElementByTagName(element, MODULES_ELEMENT);
		if (definitionsElement != null) {
			readDefinitions(definition, definitionsElement);
		}
	}

	@SuppressWarnings("unchecked")
	private void readDefinitions(ModuleDefinition moduleDefinition, Element definitionsElement) {
		List<Element> definitionElementList = DomUtils.getChildElementsByTagName(definitionsElement, MODULE_ELEMENT);

		for (Element definitionElement : definitionElementList) {
			
			Element nameElement = DomUtils.getChildElementByTagName(definitionElement, NAME_ELEMENT);
			Assert.notNull(nameElement, MODULE_ELEMENT + " must contain an element: " + NAME_ELEMENT);
			String name = DomUtils.getTextValue(nameElement);

			String overrides = readOptionalElementText(definitionElement, OVERRIDES_ELEMENT);
			String factory = readOptionalElementText(definitionElement, TYPE_ELEMENT);

			List<String> contextLocations = readContextLocations(definitionElement);
			
			SuppliedModuleDefinitionInfo definitionInfo = new SuppliedModuleDefinitionInfo(name, contextLocations, overrides, factory);

			ModuleDefinition childDefinition = createModuleDefinition(moduleDefinition, definitionInfo);

			readChildDefinitions(childDefinition, definitionElement);
		}
	}

	private String readOptionalElementText(Element definitionElement, String elementName) {
		Element element = DomUtils.getChildElementByTagName(definitionElement, elementName);
		String text = null;
		if (element != null)
			text = DomUtils.getTextValue(element);
		return text;
	}

	protected ModuleDefinition createModuleDefinition(ModuleDefinition moduleDefinition, SuppliedModuleDefinitionInfo definitionInfo) {
		ModuleDefinition definition = null;
		
		String name = definitionInfo.getName();
		String type = definitionInfo.getType();
		String overrides = definitionInfo.getOverrides();
		List<String> contextLocations = definitionInfo.getContextLocations();
		
		String[] locationsArray = contextLocations.toArray(new String[contextLocations.size()]);

		boolean isBeanSetDefinition = isBeanSetDefinition(type, overrides);

		if (isBeanSetDefinition) {
			definition = new SimpleBeansetModuleDefinition(moduleDefinition, name, locationsArray, overrides);
		}
		else {
			definition = new SimpleModuleDefinition(moduleDefinition, name, locationsArray);
		}
		return definition;
	}

	boolean isBeanSetDefinition(String type, String overrides) {
		boolean isBeanSetDefinition = overrides != null || ModuleTypes.APPLICATION_WITH_BEANSETS.equalsIgnoreCase(type);
		return isBeanSetDefinition;
	}

	private RootModuleDefinition getRootModuleDefinition(Element root) {
		List<String> locationNames = readContextLocations(root, CONTEXT_LOCATIONS_ELEMENT, CONTEXT_LOCATION_ELEMENT);

		// extra check to make sure parent definition had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(root, CONTEXT_LOCATIONS_ELEMENT), ROOT_MODULE_ELEMENT
					+ " must contain a child element:" + CONTEXT_LOCATION_ELEMENT);
		}
		
		List<String> projectNames = readContextLocations(root, ROOT_PROJECT_NAMES_ELEMENT, NAME_ELEMENT);

		// extra check to make sure parent definition had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(root, ROOT_PROJECT_NAMES_ELEMENT), ROOT_MODULE_ELEMENT
					+ " must contain a child element:" + ROOT_PROJECT_NAMES_ELEMENT);
		}

		//FIXME add project names
		RootModuleDefinition rootModuleDefinition = new SimpleRootModuleDefinition(projectNames, locationNames);
		return rootModuleDefinition;
	}

	@SuppressWarnings("unchecked")
	private List<String> readContextLocations(Element root) {
		return readContextLocations(root, CONTEXT_LOCATIONS_ELEMENT, CONTEXT_LOCATION_ELEMENT);
	}
	
	@SuppressWarnings("unchecked")
	private List<String> readContextLocations(Element root, String containerElement, String singleElement) {
		Element children = DomUtils.getChildElementByTagName(root, containerElement);
		List<String> locationNames = new ArrayList<String>();
		if (children != null) {
			List<Element> childrenList = DomUtils.getChildElementsByTagName(children,
					singleElement);

			for (Element childElement : childrenList) {
				String textValue = DomUtils.getTextValue(childElement);
				Assert.isTrue(StringUtils.hasText(textValue), singleElement
						+ " element cannot contain empty text");
				locationNames.add(textValue);
			}
			Assert.isTrue(!childrenList.isEmpty(), containerElement + " cannot be empty");
		}
		return locationNames;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	


}
