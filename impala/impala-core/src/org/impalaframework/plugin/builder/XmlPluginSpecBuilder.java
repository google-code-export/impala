package org.impalaframework.plugin.builder;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.spec.SimpleBeansetPluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlPluginSpecBuilder implements PluginSpecBuilder {

	String PARENT_ELEMENT = "parent";

	String CONTEXT_LOCATIONS_ELEMENT = "context-locations";

	String CONTEXT_LOCATION_ELEMENT = "context-location";
	
	String TYPE_ELEMENT = "type";

	String PLUGINS_ELEMENT = "plugins";

	String PLUGIN_ELEMENT = "plugin";

	String NAME_ELEMENT = "name";

	String OVERRIDES_ELEMENT = "overrides";

	final Logger logger = LoggerFactory.getLogger(XmlPluginSpecBuilder.class);

	private Resource resource;

	private XmlSpecDocumentLoader xmlSpecLoader;

	public XmlPluginSpecBuilder() {
		super();
		this.xmlSpecLoader = new XmlSpecDocumentLoader();
	}

	public ParentSpec getParentSpec() {
		Document document = xmlSpecLoader.loadDocument(resource);

		Element root = document.getDocumentElement();
		ParentSpec parentSpec = getParentSpec(root);

		readChildPlugins(parentSpec, root);

		return parentSpec;
	}

	private void readChildPlugins(PluginSpec parentSpec, Element element) {
		Element pluginsElement = DomUtils.getChildElementByTagName(element, PLUGINS_ELEMENT);
		if (pluginsElement != null) {
			readPlugins(parentSpec, pluginsElement);
		}
	}

	@SuppressWarnings("unchecked")
	private void readPlugins(PluginSpec pluginSpec, Element pluginsElement) {
		List<Element> pluginElementList = DomUtils.getChildElementsByTagName(pluginsElement, PLUGIN_ELEMENT);

		for (Element pluginElement : pluginElementList) {
			
			Element nameElement = DomUtils.getChildElementByTagName(pluginElement, NAME_ELEMENT);
			Assert.notNull(nameElement, PLUGIN_ELEMENT + " must contain an element: " + NAME_ELEMENT);
			String name = DomUtils.getTextValue(nameElement);

			String overrides = readOptionalElementText(pluginElement, OVERRIDES_ELEMENT);
			String factory = readOptionalElementText(pluginElement, TYPE_ELEMENT);

			List<String> contextLocations = readContextLocations(pluginElement);
			
			SuppliedPluginInfo pluginInfo = new SuppliedPluginInfo(name, contextLocations, overrides, factory);

			PluginSpec childPluginSpec = createPluginSpec(pluginSpec, pluginInfo);

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

	protected PluginSpec createPluginSpec(PluginSpec pluginSpec, SuppliedPluginInfo pluginInfo) {
		PluginSpec childPluginSpec = null;
		
		String name = pluginInfo.getName();
		String type = pluginInfo.getType();
		String overrides = pluginInfo.getOverrides();

		//FIXME test this condition
		if (overrides != null || PluginTypes.APPLICATION_WITH_BEANSETS.equalsIgnoreCase(type)) {
			childPluginSpec = new SimpleBeansetPluginSpec(pluginSpec, name, overrides);
		}
		else {
			childPluginSpec = new SimplePluginSpec(pluginSpec, name);
		}
		return childPluginSpec;
	}

	private ParentSpec getParentSpec(Element root) {
		List<String> locationNames = readContextLocations(root);

		// extra check to make sure parent spec had a context-locations element
		if (locationNames.isEmpty()) {
			Assert.notNull(DomUtils.getChildElementByTagName(root, CONTEXT_LOCATIONS_ELEMENT), PARENT_ELEMENT
					+ " must contain a child element:" + CONTEXT_LOCATION_ELEMENT);
		}

		ParentSpec parentSpec = new SimpleParentSpec(locationNames);
		return parentSpec;
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
