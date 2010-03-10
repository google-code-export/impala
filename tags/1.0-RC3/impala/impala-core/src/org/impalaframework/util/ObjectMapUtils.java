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

package org.impalaframework.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.springframework.util.Assert;

public class ObjectMapUtils {
    
    private static Log logger = LogFactory.getLog(ObjectMapUtils.class);   
    
    /**
     * Creates map from array where keys and values are alternate values
     */
    @SuppressWarnings("unchecked")
    public static Map newMap(Object... keysAndValues) {
        Map map = new LinkedHashMap();
        for (int i = 0; i < keysAndValues.length; i++) {
            Object key = keysAndValues[i];
            i++;
            final Object value;
            if (i < keysAndValues.length) {
                value = keysAndValues[i];
            } else {
                value = null;
            }
            map.put(key, value);
        }
        return map;
    }
    
    public static <T extends Object> void maybeOverwrite(Map<String, T> initial, Map<String, T> overwriting, String contextDescription) {
        Assert.notNull(initial);
        
        if (overwriting != null) {
            putAll(initial, overwriting, contextDescription, false);
        }
    }
    
    public static <T extends Object> void maybeOverwriteToLowerCase(Map<String, T> initial, Map<String, T> overwriting, String contextDescription) {
        Assert.notNull(initial);
        
        if (overwriting != null) {
            putToLowerCase(initial, overwriting, contextDescription);
        }
    }
    
    public static<T extends Object> void putToLowerCase(Map<String, T> initial, Map<String, T> overwriting, String contextDescription) {
        putAll(initial, overwriting, contextDescription, true);
    }
    
    private static<T extends Object> void putAll(Map<String, T> initial, Map<String, T> overwriting, String contextDescription, boolean toLowerCase) {
        final Set<String> keys = overwriting.keySet();
        for (String key : keys) {
            final String modifiedKey = toLowerCase ? key.toLowerCase() : key;
        
            final T newInstance = overwriting.get(key);
            if (newInstance != null) {
                final T existing = initial.put(modifiedKey, newInstance);
                if (existing != null) {
                    logger.info(contextDescription + ": Replaced value for key [" + key + "] with instance of class " +newInstance.getClass().getName());
                    logger.info(contextDescription + ": Previous value for key [" + key + "] was instance of class " +newInstance.getClass().getName());
                } else {
                    logger.info(contextDescription + ": Added new entry for key [" + key + "] with instance of class " +newInstance.getClass().getName());
                }
            }
        }
    }
    
    public static Integer readInteger(Map<String, Object> map, String attributeName) {
        Object object = map.get(attributeName);
    
        if (object == null) {
            return null;
        }

        String toString = object.toString();
        
        if (toString.trim().length() == 0) {
            return null;
        }
        
        try {
            Integer intValue = Integer.parseInt(toString);
            return intValue;
        }
        catch (NumberFormatException e) {
            throw new InvalidStateException("Attribute with name '" + attributeName + "', and value '"
                    + object + "' is not an integer");
        }
    }
    
    public static Double readDouble(Map<String, Object> map, String attributeName) {
        Object object = map.get(attributeName);
    
        if (object == null) {
            return null;
        }

        String toString = object.toString();
        
        if (toString.trim().length() == 0) {
            return null;
        }
        
        try {
            return Double.parseDouble(toString);
        }
        catch (NumberFormatException e) {
            throw new InvalidStateException("Attribute with name '" + attributeName + "', and value '"
                    + object + "' is not a double");
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readMap(Map<String, Object> map, String attributeName) {
        Object object = map.get(attributeName);
    
        if (object == null) {
            return null;
        }
        
        try {
            Map<String, Object> toReturn = (Map<String, Object>) object;
            return toReturn;
        }
        catch (ClassCastException e) {
            throw new InvalidStateException("Attribute with name '" + attributeName + "', and value '"
                    + object + "' is not a valid map");
        }
    }
    
    public static Map<String, String> readStringMap(Map<String, Object> map, String attributeName) {
        Map<String, Object> objectMap = readMap(map, attributeName);
        if (objectMap == null) {
            return null;
        }
        
        Map<String,String> stringMap = new HashMap<String, String>();
        Set<String> keys = objectMap.keySet();
        for (String key : keys) {
            String value = null;
            Object object = objectMap.get(key);
            if (object != null) {
                value = object.toString();
            }
            stringMap.put(key, value);
        }
        return stringMap;
    }
    
    public static String readString(Map<String, Object> map, String attributeName) {
        Object object = map.get(attributeName);
    
        if (object == null) {
            return null;
        }
        
        return object.toString();
    }

    /**
     * Returns first value entry in map.
     */
    public static <T extends Object> T getFirstValue(Map<?, T> map) {
        Collection<T> values = map.values();
        if (values.size() > 0) {
            return values.iterator().next();
        }
        return null;
    }
    
    /**
     * Returns first value entry in map.
     */
    public static <T extends Object> T getFirstKey(Map<T,?> map) {
        Collection<T> values = map.keySet();
        if (values.size() > 0) {
            return values.iterator().next();
        }
        return null;
    }

}
