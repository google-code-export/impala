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

package org.impalaframework.classloader;

import junit.framework.TestCase;

public class NonDelegatingResourceClassLoaderTest extends TestCase {

    public final void testGetResourceString() throws Exception {
        BaseURLClassLoader c = ClassLoaderTestUtils.getLoader("files/classlocation1");
        assertEquals("Location1resource text", ClassLoaderTestUtils.readResource(c, "location1resource.txt"));
        assertNotNull(ClassLoaderTestUtils.readResource(c, "beanset.properties"));
        
        NonDelegatingResourceClassLoader nd = new NonDelegatingResourceClassLoader(c);
        assertEquals("Location1resource text", ClassLoaderTestUtils.readResource(nd, "location1resource.txt"));
        assertNull(nd.getResourceAsStream("beanset.properties"));
        
    }
    
}
