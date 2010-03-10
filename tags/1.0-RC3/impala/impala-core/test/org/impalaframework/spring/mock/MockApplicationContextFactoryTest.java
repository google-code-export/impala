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

package org.impalaframework.spring.mock;

import junit.framework.TestCase;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class MockApplicationContextFactoryTest extends TestCase {

    public final void testMockContext() {
        MockApplicationContextFactory factory = new MockApplicationContextFactory();
        ApplicationContext mockContext = factory.mockContext();

        assertNull(mockContext.getBean("mybean"));
        assertFalse(mockContext.containsBean("mybean"));
        
        Object object = new Object();
        factory.putBean("mybean", object);
        assertSame(object, mockContext.getBean("mybean"));

        assertTrue(mockContext.containsBean("mybean"));
        assertEquals(Object.class, mockContext.getType("mybean"));
    }
    
    public final void testStrictContext() {
        MockApplicationContextFactory factory = new MockApplicationContextFactory(true);
        ApplicationContext mockContext = factory.mockContext();

        try {
            mockContext.getBean("mybean");
            fail();
        }
        catch (NoSuchBeanDefinitionException e) {
            assertEquals("No bean named 'mybean' is defined", e.getMessage());
        }
        
        assertFalse(mockContext.containsBean("mybean"));
        
        Object object = new Object();
        factory.putBean("mybean", object);
        assertSame(object, mockContext.getBean("mybean"));

        assertTrue(mockContext.containsBean("mybean"));

        assertEquals(Object.class, mockContext.getType("mybean"));
    }

}
