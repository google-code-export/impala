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

package org.impalaframework.service.filter;

import java.util.LinkedList;

import junit.framework.TestCase;

import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.springframework.util.ClassUtils;

public class TypeServiceReferenceFilterTest extends TestCase {

    private TypeServiceReferenceFilter filter;
    private ClassLoader classLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        filter = new TypeServiceReferenceFilter();
        classLoader = ClassUtils.getDefaultClassLoader();
    }
    
    public void testMatches() {
        assertFalse(filter.matches(new StaticServiceRegistryEntry("value1", "beanName", "moduleName", classLoader)));
        
        filter.setType(String.class);
        assertTrue(filter.matches(new StaticServiceRegistryEntry("value1", "beanName", "moduleName", classLoader)));
        assertFalse(filter.matches(new StaticServiceRegistryEntry(new Integer(1), "beanName", "moduleName", classLoader)));
    }
    
    public void testCollectionMatchesAll() {
        TypeServiceReferenceFilter filter = new TypeServiceReferenceFilter();
        
        LinkedList<Class<?>> list = new LinkedList<Class<?>>();
        list.add(String.class);
        list.add(Integer.class);
        filter.setTypes(list);
        assertFalse(filter.matches(new StaticServiceRegistryEntry("value1", "beanName", "moduleName", classLoader)));
        assertFalse(filter.matches(new StaticServiceRegistryEntry(new Integer(1), "beanName", "moduleName", classLoader)));
    }
    
    public void testCollectionMatchesAny() {
        TypeServiceReferenceFilter filter = new TypeServiceReferenceFilter();
        filter.setMatchAny(true);
        
        LinkedList<Class<?>> list = new LinkedList<Class<?>>();
        list.add(String.class);
        list.add(Integer.class);
        filter.setTypes(list);
        assertTrue(filter.matches(new StaticServiceRegistryEntry("value1", "beanName", "moduleName", classLoader)));
        assertTrue(filter.matches(new StaticServiceRegistryEntry(new Integer(1), "beanName", "moduleName", classLoader)));
    }


}
