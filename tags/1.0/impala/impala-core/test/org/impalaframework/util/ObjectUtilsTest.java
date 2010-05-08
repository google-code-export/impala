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

import junit.framework.TestCase;

import org.impalaframework.exception.ExecutionException;

public class ObjectUtilsTest extends TestCase {

    public void testCastSucceeds() throws Exception {
        Object o = new Integer(1);
        Integer castValue = ObjectUtils.cast(o, Integer.class);
        assertSame(o, castValue);
    }
    
    public void testCastFails() throws Exception {
        Object o = new Integer(1);
        try {
            ObjectUtils.cast(o, String.class);
        } catch (ExecutionException e) {
            assertTrue(e.getMessage().contains(" is not an instance of String"));
        }
    }
    
    public void testCastNull() throws Exception {
        assertNull(ObjectUtils.cast(null, String.class));
    }
    
}
