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

import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class CollectionStringUtilsTest extends TestCase {

    public void testParsePropertiesFromString() {
        String input = " name1 = 1 \n name2= value 2, name3 = value3 ";
        
        Map<String, String> map = CollectionStringUtils.parsePropertiesFromString(input);
        assertEquals(3, map.size());
        assertEquals("1", map.get("name1"));
        assertEquals("value 2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
        
        Map<String, String> mapWithMissingEquals = CollectionStringUtils.parsePropertiesFromString("name1 no equals 1 \nname2= value2,name3 = value3");
        assertEquals(2, mapWithMissingEquals.size());
        assertTrue(mapWithMissingEquals.containsKey("name2"));
        assertTrue(mapWithMissingEquals.containsKey("name3"));
    }
    
    public void testParseMapFromString() {
        String input = " name1 = 1 \n name2= 2007-06-11 16:15:32, name3 = value3 ";
        
        Map<String, Object> map = CollectionStringUtils.parseMapFromString(input);
        assertEquals(3, map.size());
        assertEquals(new Long(1), map.get("name1"));
        assertTrue(map.get("name2") instanceof Date);
        assertEquals("value3", map.get("name3"));
    }
    
    public void testParseStringList() throws Exception {
        String input = "value 1, 2007-06-11 16:15:32 \n 3 ";
        List<String> list = CollectionStringUtils.parseStringList(input);
        
        assertEquals(3, list.size());
        assertEquals("value 1", list.get(0));
        assertEquals("2007-06-11 16:15:32", list.get(1));
        assertEquals("3", list.get(2));
    }

    public void testParseObjectList() throws Exception {
        String input = "value 1, 2007-06-11 16:15:32 \n 3 ";
        List<Object> list = CollectionStringUtils.parseObjectList(input);
        assertEquals(3, list.size());
        assertEquals("value 1", list.get(0));
        assertTrue(list.get(1) instanceof Date);
        assertEquals(new Long(3), list.get(2));
    }
    
}
