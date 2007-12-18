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

	private XmlSpecDocumentLoader xmlSpecLoader;

	public XmlModuleDefinitionSource() {
		super();
		this.xmlSpecLoader = new XmlSpecDocumentLoader();
	}

	public RootModuleDefinition getModuleDefintion() {
		Document document = xmlSpecLoader.loadDocument(resource);

		Element root = document.getDocumentElement();
		RootModuleDefinition rootModuleDefinition = getParentSpec(root);

		readChildPlugins(rootModuleDefinition, root);

		return rootModuleDefinition;
	}

	private void readChildPlugins(ModuleDefinition parentSpec, Element element) {
		Element pluginsElement = DomUtils.getChildElementByTagName(element, PLUGINS_ELEMENT);
		if (pluginsElement != null) {
			readPlugins(parentSpec, pluginsElement);
		}
	}

	@SuppressWarnings("unchecked")
	private void readPlugins(ModuleDefinition moduleDefinition, Element pluginsElement) {
		List<Element> pluginElementList = DomUtils.getChildElementsByTagName(pluginsElement, PLUGIN_ELEMENT);

		for (Element pluginElement : pluginElementList) {
			
			Element nameElement = DomUtils.getChildElementByTagName(pluginElement, NAME_ELEMENT);
			Assert.notNull(nameElement, PLUGIN_ELEMENT + " must contain an element: " + NAME_ELEMENT);
			String name = DomUtils.getTextValue(nameElement);

			String overrides = readOptionalElementText(pluginElement, OVERRIDES_ELEMENT);
			String factory = readOptionalElementText(pluginElement, TYPE_ELEMENT);

			List<String> contextLocations = readContextLocations(pluginElement);
			
			SuppliedModuleDefinitionInfo pluginInfo = new SuppliedModuleDefinitionInfo(name, contextLocations, overrides, factory);

			ModuleDefinition childPluginSpec = createPluginSpec(moduleDefinition, pluginInfo);

			readChildPlugins(childPluginSpec, pluginElement);
		}
	}

	private String readOptionalElementText(Element pluginElement, String elementName) {
		Element element = DomUtils.getChildElementByTagName(pluginElement, elementName);
		String text = null;
		if (element != null)
			text = DomUtils.getTextValue(element);
		return text;
	}

	protected ModuleDefinition createPluginSpec(ModuleDefinition moduleDefinition, SuppliedModuleDefinitionInfo pluginInfo) {
		ModuleDefinition childPluginSpec = null;
		
		String name = pluginInfo.getName();
		String type = pluginInfo.getType();
		String overrides = pluginInfo.getOverrides();

		boolean isBeanSetSpec = isBeanSetSpec(type, overrides);

		if (isBeanSetSpec) {
			childPluginSpec = new SimpleBeansetModuleDefinition(moduleDefinition, name, overrides);
		}
		else {
			childPluginSpec = new SimpleModuleDefinition(moduleDefinition, name);
		}
		return childPluginSpec;
	}

	boolean isBeanSetSpec(String type, String overrides) {
		boolean isBeanSetSpec = overrides != null || ModuleTypes.APPLICATION_WITH_BEANSETS.equalsIgnoreCase(type);
		return isBeanSetSpec;
	}

	private RootModuleDefinition getParentSpec(Element root) {
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
