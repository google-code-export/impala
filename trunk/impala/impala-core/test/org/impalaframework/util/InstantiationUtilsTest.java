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

import junit.framework.TestCase;

import org.impalaframework.exception.ExecutionException;

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
            @SuppressWarnings("unused")
            String instantiate = InstantiationUtils.instantiate("unknown");
            fail();
        }
        catch (ExecutionException e) {
            assertEquals("Unable to find class of type 'unknown'", e.getMessage());
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
