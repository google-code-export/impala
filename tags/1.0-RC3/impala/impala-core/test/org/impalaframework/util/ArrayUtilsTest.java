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

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

public class ArrayUtilsTest extends TestCase {

    public void testToList() {
        assertEquals(new ArrayList<String>(), ArrayUtils.toList(new String[0]));
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        assertEquals(list, ArrayUtils.toList(new String[]{"a", "b"}));
    }
    
    public void testTrim() throws Exception {
        String input = "a , b , c,d";
        String[] result = ArrayUtils.trim(input.split(","));
        assertTrue(Arrays.equals("a,b,c,d".split(","), result));
    }

}
