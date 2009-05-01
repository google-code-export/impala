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


public class MapStringUtils {

    /**
     * Generates map of String to String name value pairs, assuming that individual pairings are
     * separated by a new line or comma, and that name to value pairs are separated by equals
     * @param mapString the input String
     * @return a String to String map. Uses {@link LinkedHashMap} so that the ordering of the key
     * value pairs is maintained
     */
    public static Map<String,String> parseMapFromString(String mapString) {
        String delimiters = ",\n";
        Assert.notNull(mapString, "map string cannot be null");
        String[] pairings = StringUtils.tokenizeToStringArray(mapString, delimiters);
        
        Map<String,String> map = new LinkedHashMap<String,String>();
        for (String pair : pairings) {
            int equalsIndex = pair.indexOf('=');
            if (equalsIndex > 0) {
                map.put(pair.substring(0, equalsIndex).trim(), pair.substring(equalsIndex+1).trim());
            }
        }
        
        return map;
    }
    
}
