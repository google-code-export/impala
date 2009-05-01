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

import java.util.Map;

import junit.framework.TestCase;

public class MapStringUtilsTest extends TestCase {

    public void testParseMapFromString() {
        String input = " name1 = 1 \n name2= value 2, name3 = value3 ";
        
        Map<String, String> map = MapStringUtils.parseMapFromString(input);
        assertEquals(3, map.size());
        assertEquals("1", map.get("name1"));
        assertEquals("value 2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
        
        Map<String, String> mapWithMissingEquals = MapStringUtils.parseMapFromString("name1 no equals 1 \nname2= value2,name3 = value3");
        assertEquals(2, mapWithMissingEquals.size());
        assertTrue(mapWithMissingEquals.containsKey("name2"));
        assertTrue(mapWithMissingEquals.containsKey("name3"));
    }

}
