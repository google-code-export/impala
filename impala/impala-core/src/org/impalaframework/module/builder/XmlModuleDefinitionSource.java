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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlModuleDefinitionSource implements ModuleDefinitionSource {

	String PARENT_ELEMENT = "parent";

	String CONTEXT_LOCATIONS_ELEMENT = "context-locations";

	String CONTEXT_LOCATION_ELEMENT = "context-location";
	
	String TYPE_ELEMENT = "type";

	String PLUGINS_ELEMENT = "plugins";

	String PLUGIN_ELEMENT = "plugin";

	String NAME_ELEMENT = "name";

	String OVERRIDES_ELEMENT = "overrides";

	final Logger logger = LoggerFactory.getLogger(XmlModuleDefinitionSource.class);

	private Resource resource;

	private XmlModulelDefinitionDocumentLoader xmlSpecLoader;

	public XmlModuleDefinitionSource() {
		super();
		this.xmlSpecLoader = new XmlModulelDefinitionDocumentLoader();
	}

	public RootModuleDefinition getModuleDefinition() {
		Document document = xmlSpecLoader.loadDocument(resource);

		Element root = document.getDocumentElement();
		RootModuleDefinition rootModuleDefinition = getRootModuleDefinition(root);

		readChildDefinitions(rootModuleDefinition, root);

		return rootModuleDefinition;
	}

	private void readChildDefinitions(ModuleDefinition definition, Element element) {
		Element definitionsElement = DomUtils.getChildElementByTagName(element, PLUGINS_ELEMENT);
		if (definitionsElement != null) {
			readDefinitions(definition, definitionsElement);
		}
	}

	@SuppressWarnings("unchecked")
	private void readDefinitions(ModuleDefinition moduleDefinition, Element definitionsElement) {
		List<Element> definitionElementList = DomUtils.getChildElementsByTagName(definitionsElement, PLUGIN_ELEMENT);

		for (Element definitionElement : definitionElementList) {
			
			Element nameElement = DomUtils.getChildElementByTagName(definitionElement, NAME_ELEMENT);
			Assert.notNull(nameElement, PLUGIN_ELEMENT + " must contain an element: " + NAME_ELEMENT);
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

		boolean isBeanSetDefinition = isBeanSetDefinition(type, overrides);

		if (isBeanSetDefinition) {
			definition = new SimpleBeansetModuleDefinition(moduleDefinition, name, overrides);
		}
		else {
			definition = new SimpleModuleDefinition(moduleDefinition, name);
		}
		return definition;
	}

	boolean isBeanSetDefinition(String type, String overrides) {
		boolean isBeanSetSpec = overrides != null || ModuleTypes.APPLICATION_WITH_BEANSETS.equalsIgnoreCase(type);
		return isBeanSetSpec;
	}

	private RootModuleDefinition getRootModuleDefinition(Element root) {
		List<String> locationNames = readContextLocations(root);

		// extra check to make sure parent spec had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(root, CONTEXT_LOCATIONS_ELEMENT), PARENT_ELEMENT
					+ " must contain a child element:" + CONTEXT_LOCATION_ELEMENT);
		}

		RootModuleDefinition rootModuleDefinition = new SimpleRootModuleDefinition(locationNames);
		return rootModuleDefinition;
	}

	@SuppressWarnings("unchecked")
	private List<String> readContextLocations(Element root) {
		Element contextLocationsElement = DomUtils.getChildElementByTagName(root, CONTEXT_LOCATIONS_ELEMENT);
		List<String> locationNames = new ArrayList<String>();
		if (contextLocationsElement != null) {
			List<Element> contextLocationElementList = DomUtils.getChildElementsByTagName(contextLocationsElement,
					CONTEXT_LOCATION_ELEMENT);

			for (Element contextLocationElement : contextLocationElementList) {
				String textValue = DomUtils.getTextValue(contextLocationElement);
				Assert.isTrue(StringUtils.hasText(textValue), CONTEXT_LOCATION_ELEMENT
						+ " element cannot contain empty text");
				locationNames.add(textValue);
			}
			Assert.isTrue(!contextLocationElementList.isEmpty(), CONTEXT_LOCATIONS_ELEMENT + " cannot be empty");
		}
		return locationNames;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	


}
