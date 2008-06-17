package org.impalaframework.module.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.builder.ModuleElementNames;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class TypeReaderUtils {

	public static TypeReader getTypeReader(Map<String, TypeReader> typeReaders, String typeName) {
		TypeReader typeReader = typeReaders.get(typeName.toLowerCase());
		if (typeReader == null) {
			throw new ConfigurationException("No " + TypeReader.class.getName() + " specified for type '" + typeName + "'");
		}
		return typeReader;
	}

	@SuppressWarnings("unchecked")
	static List<String> readContextLocations(Element root) {
		return TypeReaderUtils.readContextLocations(root, ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, ModuleElementNames.CONTEXT_LOCATION_ELEMENT);
	}

	@SuppressWarnings("unchecked")
	static List<String> readContextLocations(Element root, String containerElement, String singleElement) {
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

}
