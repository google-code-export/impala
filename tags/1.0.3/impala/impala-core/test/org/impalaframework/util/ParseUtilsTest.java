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

import java.util.Date;

import junit.framework.TestCase;

public class ParseUtilsTest extends TestCase {
    
    public void testParseObject() {
        assertNull(ParseUtils.parseObject(null));
        assertTrue(ParseUtils.parseObject("2007-06-11 16:15:32") instanceof Date);
        assertTrue(ParseUtils.parseObject("10") instanceof Long);
        assertTrue(ParseUtils.parseObject("10.0") instanceof Double);
        assertTrue(ParseUtils.parseObject("true") instanceof Boolean);
        assertTrue(ParseUtils.parseObject("a string") instanceof String);
    }
    
}
