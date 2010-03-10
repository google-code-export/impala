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
import org.impalaframework.exception.InvalidStateException;

public class InstantiationUtilsTest extends TestCase {

    public final void testInstantiate() {
        InstantiationUtilsTest instantiate = InstantiationUtils.instantiate(InstantiationUtilsTest.class.getName());
        assertEquals(this.getClass(), instantiate.getClass());
    }
    
    public final void testWrongClass() {
        try {
            @SuppressWarnings("unused")
            String instantiate = InstantiationUtils.instantiate(InstantiationUtilsTest.class.getName());
            fail();
        }
        catch (ClassCastException e) {
        }
    }
    
    public final void testClassNotFound() {
        try {
            InstantiationUtils.instantiate("unknown");
            fail();
        }
        catch (ExecutionException e) {
            assertEquals("Unable to find class of type 'unknown'", e.getMessage());
        }
    }    
    
    public final void testNoNoargs() {
        try {
            InstantiationUtils.instantiate(Integer.class.getName());
            fail();
        }
        catch (InvalidStateException e) {
            assertEquals("Cannot instantiate class 'class java.lang.Integer' as it has no no-args constructor", e.getMessage());
        }
    }
    
    public final void testClassWithPrivateConstructor() {
        ClassWithPrivateConstructor instantiate = InstantiationUtils.instantiate(ClassWithPrivateConstructor.class.getName());
        assertNotNull(instantiate);
    }

}

class ClassWithPrivateConstructor {

    private ClassWithPrivateConstructor() {
        super();
    }
    
}
