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

package org.impalaframework.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


public abstract class MapStringUtils {

    /**
     * Generates map of String to String name value pairs, assuming that individual pairings are
     * separated by a new line or comma, and that name to value pairs are separated by equals
     * @param mapString the input String
     * @return a String to String map. Uses {@link LinkedHashMap} so that the ordering of the key
     * value pairs is maintained
     */
    public static Map<String,String> parsePropertiesFromString(String mapString) {
        //FIXME should have escape syntax for ,
        return parsePropertiesFromString(mapString, ",\n");
    }

    /**
     * As in {@link #parsePropertiesFromString(String)}, but with allowing the user to specify the 
     * delimiters between pairings
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parsePropertiesFromString(String mapString, String delimiters) {
        return parseMap(mapString, delimiters, stringConverter);
    }
    
    /**
     * Generates map of String to Object name value pairs, assuming that individual pairings are
     * separated by a new line or comma, and that name to value pairs are separated by equals
     * @param mapString the input String
     * @return a String to Object map. Uses {@link ParseUtils#parseObject(String)} to convert 
     * String values to map values.
     * Uses {@link LinkedHashMap} so that the ordering of the key
     * value pairs is maintained
     */
    public static Map<String,Object> parseMapFromString(String mapString) {
        //FIXME should have escape syntax for ,
        return parseMapFromString(mapString, ",\n");
    }
    
    /**
     * As in {@link #parseMapFromString(String)}, but with allowing the user to specify the 
     * delimiters between pairings
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseMapFromString(String mapString, String delimiters) {
        return parseMap(mapString, delimiters, objectConverter);
    }

    
    @SuppressWarnings("unchecked")
    private static Map parseMap(String mapString,
            String delimiters, ValueConverter valueConverter) {
        Assert.notNull(mapString, "map string cannot be null");
        Assert.notNull(delimiters, "delimiters cannot be null");
        
        String[] pairings = StringUtils.tokenizeToStringArray(mapString, delimiters);
        Map map = new LinkedHashMap();
        for (String pair : pairings) {
            int equalsIndex = pair.indexOf('=');
            if (equalsIndex > 0) {
                Object value = valueConverter.convertValue(pair.substring(equalsIndex+1).trim());
                map.put(pair.substring(0, equalsIndex).trim(), value);
            }
        }
        
        return map;
    }
    
    private static interface ValueConverter {
        Object convertValue(String text);
    }
    
    private static ValueConverter stringConverter = new ValueConverter() {

        public Object convertValue(String text) {
            return text;
        }
        
    };
    
    private static ValueConverter objectConverter = new ValueConverter() {

        public Object convertValue(String text) {
            return ParseUtils.parseObject(text);
        }
        
    };
    
}
