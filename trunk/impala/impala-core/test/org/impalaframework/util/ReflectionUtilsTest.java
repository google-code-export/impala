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

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class ReflectionUtilsTest extends TestCase {
    
    public void testFindConstructors() throws Exception {
        final Constructor<?> constructor = ReflectionUtils.findConstructor(String.class, new Class[]{ String.class });
        assertNotNull(constructor);
        final Object instance = ReflectionUtils.invokeConstructor(constructor, new String[]{"hello"}, true);
        assertEquals("hello", instance);
    }
    
    public void testFindInterface() {
        final Class<?>[] interfaces = ReflectionUtils.findInterfaces(new ClassBottom());
        System.out.println(Arrays.asList(interfaces));
        final Class<?>[] expected = new Class[]{ Interface3.class, Interface2.class, Interface1.class };
        System.out.println(Arrays.asList(expected));
        assertTrue(Arrays.equals(expected, interfaces));
    }
    
    public void testInvokeMethod() {

        HashMap<?, ?> map = new HashMap<Object, Object>() {
            private static final long serialVersionUID = 1L;
            public String toString() {
                return "a map";
            }
        };

        try {

            ReflectionUtils.invokeMethod(new TestExample(), "METHOD", map);
            fail();
        }
        catch (UnsupportedOperationException e) {
            assertEquals("No method compatible with method: METHOD, args: [a map]", e.getMessage());
        }
        
        ReflectionUtils.invokeMethod(new TestExample(), "method1", map);
        assertEquals("me", ReflectionUtils.invokeMethod(new TestExample(), "method2"));
    }
    
    interface Interface1 {
    }

    interface Interface2 {
    }
    
    interface Interface3 {
    }

    class ClassTop implements Interface1 {
    }
    
    class ClassMiddle extends ClassTop implements Interface2 {
    }
    
    class ClassBottom extends ClassMiddle implements Interface3 {
    }
}

class TestExample {
    void method1(Map<?, ?> context) {
    }

    @SuppressWarnings("unused")
    private String method2() {
        return "me";
    }
}
