/*
 * Copyright 2007-2010 the original author or authors.
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
 * Copyright 2007-2010 the original author or authors.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.module.spi.TypeReader;
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
     * Reads the context locations from the XML {@link Element} instance using the <code>config-locations</code> subelement.
     */
    static List<String> readContextLocations(Element root) {
        return TypeReaderUtils.readXmlElementValues(root, ModuleElementNames.CONFIG_LOCATIONS_ELEMENT, ModuleElementNames.CONFIG_LOCATION_ELEMENT);
    }
    
    /**
     * Reads the dependencies from the XML {@link Element} instance using the <code>dependencies</code> subelement.
     */
    static List<String> readDependencyNames(Element root) {
        return TypeReaderUtils.readXmlElementValues(root, ModuleElementNames.DEPENDENCIES_ELEMENT);
    }
    
    /**
     * Reads attributes from {@link Properties} instance. Removes from map 
     * properties which are represent by existing fields.
     */
    @SuppressWarnings("unchecked")
    static Map<String,String> readAttributes(Element element) {
        Map<String,String> map = new HashMap<String, String>();
        
        Element child = DomUtils.getChildElementByTagName(element, ModuleElementNames.ATTRIBUTES_ELEMENT);
        
        if (child != null) {
            final List<Element> attributes = DomUtils.getChildElementsByTagName(child, ModuleElementNames.ATTRIBUTE_ELEMENT);
            for (Element attribute : attributes) {
                final String name = attribute.getAttribute(ModuleElementNames.NAME_ELEMENT);
                Assert.isTrue(StringUtils.hasText(name), "'attribute' element contains 'name' element.");
                final String value = DomUtils.getTextValue(attribute);
                map.put(name, value);
            }
        }
        return map;
    }
    
    /**
     * Reads the context locations from the {@link Properties} instance using the <code>config-locations</code> property.
     */
    static String[] readContextLocations(Properties properties) {
        return readPropertyValues(properties, ModuleElementNames.CONFIG_LOCATIONS_ELEMENT);
    }

    /**
     * Reads the dependencies from the {@link Properties} instance using the <code>dependencies</code> property.
     */
    static String[] readDependencyNames(Properties properties) {
        return readPropertyValues(properties, ModuleElementNames.DEPENDENCIES_ELEMENT);
    }

    /**
     * Reads attributes from {@link Properties} instance. Removes from map 
     * properties which are represent by existing fields.
     */
    static Map<String,String> readAttributes(Properties properties) {
        Map<String,String> map = new HashMap<String, String>();
        final Set<Object> keys = properties.keySet();
        for (Object keyObject : keys) {
            String key = keyObject.toString();
            map.put(key, properties.getProperty(key));
        }
        map.remove(ModuleElementNames.CONFIG_LOCATIONS_ELEMENT);
        map.remove(ModuleElementNames.DEPENDENCIES_ELEMENT);
        map.remove(ModuleElementNames.NAME_ELEMENT);
        map.remove(ModuleElementNames.RUNTIME_ELEMENT);
        return map;
    }
    
    /**
     * Reads the comma-separated value of the property contained in the {@link Properties}
     * as a String array.
     */
    static String[] readPropertyValues(Properties properties, String propertyName) {
        
        String configLocations = properties.getProperty(propertyName);
        return valueToStringArray(configLocations);
    }
    
    /**
     * Reads subelements' text as a String array.
     * @param root the XML {@link Element} from which the read operation starts
     * @param containerElement the name of element which contains the subelements. e.g. <code>depends-on</code>.
     */
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
     * @param containerElement the name of element which contains the subelements. e.g. <code>config-locations</code>.
     * @param subelement the name of the element whose text represent an individual value. e.g. <code>config-location</code>
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
    
    private static String[] valueToStringArray(String configLocations) {
        String[] valuesArray = null;
        if (StringUtils.hasText(configLocations)) {
            valuesArray = StringUtils.tokenizeToStringArray(configLocations, ", ", true, true);
        } else {
            valuesArray = new String[0];
        }
        return valuesArray;
    }

    public static String readRuntime(Element definitionElement) {
        return readElementValue(definitionElement, ModuleElementNames.RUNTIME_ELEMENT);
    }

    public static String readType(Element definitionElement) {
        return readElementValue(definitionElement, ModuleElementNames.TYPE_ELEMENT);
    }

    public static String readElementValue(Element definitionElement, String elementName) {
        Element runtimeElement = DomUtils.getChildElementByTagName(definitionElement, elementName);
        String runtime = null;
        if (runtimeElement != null) {
            runtime = DomUtils.getTextValue(runtimeElement);
        }
        return runtime;
    }


}
