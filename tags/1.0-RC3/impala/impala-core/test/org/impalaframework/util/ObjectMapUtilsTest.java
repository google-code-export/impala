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

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.exception.InvalidStateException;

import junit.framework.TestCase;

public class ObjectMapUtilsTest extends TestCase {
    
    @SuppressWarnings("unchecked")
    public void testNewMap() throws Exception {
        Map map = ObjectMapUtils.newMap("k1", "v1", "k2", 2);
        assertEquals(2, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals(2, map.get("k2"));
        
        map = ObjectMapUtils.newMap("k1", "v1", "k2");
        assertEquals(2, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals(null, map.get("k2"));
        
        map = ObjectMapUtils.newMap();
        assertTrue(map.isEmpty());
    }
    
    @SuppressWarnings("unchecked")
    public void testGetFirstValue() throws Exception {
        Map map = ObjectMapUtils.newMap("k1", "v1");
        assertEquals("v1", ObjectMapUtils.getFirstValue(map));
        
        map = ObjectMapUtils.newMap();
        assertEquals(null, ObjectMapUtils.getFirstValue(map));
    }
    
    @SuppressWarnings("unchecked")
    public void testGetFirstKey() throws Exception {
        Map map = ObjectMapUtils.newMap("k1", "v1");
        assertEquals("k1", ObjectMapUtils.getFirstKey(map));
        
        map = ObjectMapUtils.newMap();
        assertEquals(null, ObjectMapUtils.getFirstKey(map));
    }
    
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
