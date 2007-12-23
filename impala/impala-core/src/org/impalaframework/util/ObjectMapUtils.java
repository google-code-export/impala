package org.impalaframework.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObjectMapUtils {
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
			throw new IllegalArgumentException("Attribute with name '" + attributeName + "', and value '"
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
			throw new IllegalArgumentException("Attribute with name '" + attributeName + "', and value '"
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
			throw new IllegalArgumentException("Attribute with name '" + attributeName + "', and value '"
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
