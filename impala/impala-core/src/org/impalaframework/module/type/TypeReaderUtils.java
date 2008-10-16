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
import java.util.List;

import org.impalaframework.module.ModuleElementNames;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class TypeReaderUtils {

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
