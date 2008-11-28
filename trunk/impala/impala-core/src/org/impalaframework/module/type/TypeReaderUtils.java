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

package org.impalaframework.module.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.impalaframework.module.ModuleElementNames;
import org.impalaframework.module.TypeReader;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Class for utility methods used by {@link TypeReader} implementations
 * @author Phil Zoio
 */
public class TypeReaderUtils {

	/**
	 * Reads the context locations from the XML {@link Element} instance using the <code>context-locations</code> subelement.
	 */
	@SuppressWarnings("unchecked")
	static List<String> readContextLocations(Element root) {
		return TypeReaderUtils.readXmlElementValues(root, ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, ModuleElementNames.CONTEXT_LOCATION_ELEMENT);
	}
	
	/**
	 * Reads the dependencies from the XML {@link Element} instance using the <code>dependencies</code> subelement.
	 */
	@SuppressWarnings("unchecked")
	static List<String> readDependencyNames(Element root) {
		return TypeReaderUtils.readXmlElementValues(root, ModuleElementNames.DEPENDENCIES_ELEMENT);
	}
	
	/**
	 * Reads the context locations from the {@link Properties} instance using the <code>context-locations</code> property.
	 */
	static String[] readContextLocations(Properties properties) {
		return readPropertyValues(properties, ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT);
	}

	/**
	 * Reads the dependencies from the {@link Properties} instance using the <code>dependencies</code> property.
	 */
	static String[] readDependencyNames(Properties properties) {
		return readPropertyValues(properties, ModuleElementNames.DEPENDENCIES_ELEMENT);
	}

	/**
	 * Reads the comma-separated value of the property contained in the {@link Properties}
	 * as a String array.
	 */
	static String[] readPropertyValues(Properties properties, String propertyName) {
		
		String contextLocations = properties.getProperty(propertyName);
		return valueToStringArray(contextLocations);
	}
	
	/**
	 * Reads subelements' text as a String array.
	 * @param root the XML {@link Element} from which the read operation starts
	 * @param containerElement the name of element which contains the subelements. e.g. <code>depends-on</code>.
	 */
	@SuppressWarnings("unchecked")
	static List<String> readXmlElementValues(Element root, String containerElement) {
		
		Element children = DomUtils.getChildElementByTagName(root, containerElement);
		if (children != null) {
			String value = DomUtils.getTextValue(children);
			if (value != null) {
				return Arrays.asList(valueToStringArray(value));
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Reads subelements' text as a String array.
	 * @param root the XML {@link Element} from which the read operation starts
	 * @param containerElement the name of element which contains the subelements. e.g. <code>context-locations</code>.
	 * @param subelement the name of the element whose text represent an individual value. e.g. <code>context-location</code>
	 */
	@SuppressWarnings("unchecked")
	static List<String> readXmlElementValues(Element root, String containerElement, String subelement) {
		Element children = DomUtils.getChildElementByTagName(root, containerElement);
		List<String> values = new ArrayList<String>();
		if (children != null) {
			List<Element> childrenList = DomUtils.getChildElementsByTagName(children,
					subelement);
	
			for (Element childElement : childrenList) {
				String textValue = DomUtils.getTextValue(childElement);
				Assert.isTrue(StringUtils.hasText(textValue), subelement
						+ " element cannot contain empty text");
				values.add(textValue);
			}
			Assert.isTrue(!childrenList.isEmpty(), containerElement + " cannot be empty");
		}
		return values;
	}
	
	private static String[] valueToStringArray(String contextLocations) {
		String[] valuesArray = null;
		if (StringUtils.hasText(contextLocations)) {
			valuesArray = StringUtils.tokenizeToStringArray(contextLocations, ", ", true, true);
		} else {
			valuesArray = new String[0];
		}
		return valuesArray;
	}


}
