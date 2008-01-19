package org.impalaframework.util;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.exception.InvalidStateException;

import junit.framework.TestCase;

public class ObjectMapUtilsTest extends TestCase {
	
	public void testGetIntValue() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("intvalue1", 1);
		map.put("intvalue2", "1");
		map.put("intvalue3", null);
		map.put("intvalue4", " ");
		map.put("intvalue5", "invalid");
		
		assertEquals(new Integer(1), ObjectMapUtils.readInteger(map, "intvalue1"));
		assertEquals(new Integer(1), ObjectMapUtils.readInteger(map, "intvalue2"));
		assertNull(ObjectMapUtils.readInteger(map, "intvalue3"));
		assertNull(ObjectMapUtils.readInteger(map, "intvalue4"));
		
		try {
			ObjectMapUtils.readInteger(map, "intvalue5");
			fail();
		}
		catch (InvalidStateException e) {
			assertEquals("Attribute with name 'intvalue5', and value 'invalid' is not an integer", e.getMessage());
		}
	}
	
	public void testGetStringValue() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stringvalue1", 1);
		map.put("stringvalue2", "1");
		map.put("stringvalue3", null);
		
		assertEquals("1", ObjectMapUtils.readString(map, "stringvalue1"));
		assertEquals("1", ObjectMapUtils.readString(map, "stringvalue2"));
		assertEquals(null, ObjectMapUtils.readString(map, "stringvalue3"));
	}
	
	public void testGetDoubleValue() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("doublevalue1", 1);
		map.put("doublevalue2", "1");
		map.put("doublevalue3", null);
		map.put("doublevalue4", " ");
		map.put("doublevalue5", "invalid");
		
		assertEquals(new Double(1), ObjectMapUtils.readDouble(map, "doublevalue1"));
		assertEquals(new Double(1), ObjectMapUtils.readDouble(map, "doublevalue2"));
		assertNull(ObjectMapUtils.readDouble(map, "doublevalue3"));
		assertNull(ObjectMapUtils.readDouble(map, "doublevalue4"));
		
		try {
			ObjectMapUtils.readDouble(map, "doublevalue5");
			fail();
		}
		catch (InvalidStateException e) {
			assertEquals("Attribute with name 'doublevalue5', and value 'invalid' is not a double", e.getMessage());
		}
	}
	
	public void testGetMapValue() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mapvalue1", null);
		map.put("mapvalue2", map);
		map.put("mapvalue3", "1");
		
		assertEquals(null, ObjectMapUtils.readMap(map, "mapvalue1"));
		assertEquals(map, ObjectMapUtils.readMap(map, "mapvalue2"));
		
		try {
			ObjectMapUtils.readMap(map, "mapvalue3");
			fail();
		}
		catch (InvalidStateException e) {
			assertEquals("Attribute with name 'mapvalue3', and value '1' is not a valid map", e.getMessage());
		}
	}
	
	public void testReadStringMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mapvalue1", null);
		map.put("mapvalue2", "1");
		map.put("mapvalue3", 1);
		Map<String,Object> container = new HashMap<String, Object>();
		container.put("amap", map);
		
		Map<String, String> stringMap = ObjectMapUtils.readStringMap(container, "amap");
		assertEquals(null, stringMap.get("mapvalue1"));
		assertEquals("1", stringMap.get("mapvalue2"));
		assertEquals("1", stringMap.get("mapvalue3"));
		
		assertNull(ObjectMapUtils.readStringMap(map, "nomap"));
		
	}
}
