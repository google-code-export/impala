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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.InvalidStateException;
import org.springframework.util.Assert;

public class ObjectMapUtils {
	
	public static <T extends Object> void maybeOverwrite(Map<String, T> initial, Map<String, T> overwriting, String contextDescription) {
		Assert.notNull(initial);
		
		if (overwriting != null) {
			//FIXME add logging
			initial.putAll(overwriting);
		}
	}
	
	public static <T extends Object> void maybeOverwriteToLowerCase(Map<String, T> initial, Map<String, T> overwriting, String contextDescription) {
		Assert.notNull(initial);
		
		if (overwriting != null) {
			putToLowerCase(initial, overwriting);
		}
	}
	
	public static<T extends Object> void putToLowerCase(Map<String, T> initial, Map<String, T> overwriting) {
		final Set<String> keys = overwriting.keySet();
		for (String key : keys) {
			//FIXME add logging
			initial.put(key.toLowerCase(), overwriting.get(key));
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
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
	public static String readString(Map<String, Object> map, String attributeName) {
		Object object = map.get(attributeName);
	
		if (object == null) {
			return null;
		}
		
		return object.toString();
	}

}
